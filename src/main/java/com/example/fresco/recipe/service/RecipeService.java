package com.example.fresco.recipe.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.*;
import com.example.fresco.history.domain.History;
import com.example.fresco.history.domain.repository.HistoryRepository;
import com.example.fresco.ingredient.domain.Category;
import com.example.fresco.ingredient.domain.Ingredient;
import com.example.fresco.ingredient.domain.repository.CategoryRepository;
import com.example.fresco.ingredient.domain.repository.IngredientRepository;
import com.example.fresco.recipe.controller.dto.request.CookingRequest;
import com.example.fresco.recipe.controller.dto.request.RecipeCreateRequest;
import com.example.fresco.recipe.controller.dto.response.CookingResponse;
import com.example.fresco.recipe.controller.dto.response.RecipeDetailResponse;
import com.example.fresco.recipe.controller.dto.response.RecipeListResponse;
import com.example.fresco.recipe.controller.dto.response.StockResponse;
import com.example.fresco.recipe.domain.FavoritesRecipe;
import com.example.fresco.recipe.domain.Recipe;
import com.example.fresco.recipe.domain.RecipeIngredient;
import com.example.fresco.recipe.domain.Repository.FavoriteRepository;
import com.example.fresco.recipe.domain.Repository.RecipeIngredientRepository;
import com.example.fresco.recipe.domain.Repository.RecipeRepository;
import com.example.fresco.recipe.domain.Repository.ShareRepository;
import com.example.fresco.recipe.domain.Share;
import com.example.fresco.refrigerator.domain.Refrigerator;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorIngredientRepository;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final FavoriteRepository favoriteRepository;
    private final ShareRepository shareRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final HistoryRepository historyRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public RecipeDetailResponse createRecipe(RecipeCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        Recipe recipe = Recipe.builder()
                .title(request.title())
                .steps(request.steps())
                .url(request.url())
                .user(user)
                .build();
        Recipe saved = recipeRepository.save(recipe);

        Category etcCategory = categoryRepository.findById(11L).get();

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (RecipeCreateRequest.RecipeIngredients ingredientDto : request.ingredients()) {
            String ingredientName = ingredientDto.ingredientName() == null ? null : ingredientDto.ingredientName().trim();
            if (ingredientName == null || ingredientName.isEmpty()) {
                throw new RestApiException(IngredientErrorCode.NULL_INGREDIENT);
            }
            Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                    .orElseGet(() -> ingredientRepository.save(
                            Ingredient.builder()
                                    .ingredientName(ingredientName)
                                    .category(etcCategory)
                                    .build()
                    ));


            RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .recipe(recipe)
                    .ingredient(ingredient)
                    .quantity(ingredientDto.quantity())
                    .unit(ingredientDto.unit())
                    .build();

            recipeIngredients.add(recipeIngredient);
        }

        recipeIngredientRepository.saveAll(recipeIngredients);
        List<RecipeIngredient> items = recipeIngredientRepository.findAllByRecipeId(saved.getId());
        return RecipeDetailResponse.from(saved, items);
    }

    @Transactional
    public RecipeDetailResponse getRecipeDetail(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RestApiException(RecipeErrorCode.NULL_RECIPE));
        List<RecipeIngredient> items = recipeIngredientRepository.findAllByRecipeId(recipeId);
        return RecipeDetailResponse.from(recipe, items);
    }

    @Transactional
    public List<RecipeListResponse> getRecipeList(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        List<Recipe> recipes = recipeRepository.findAll();

        Set<Long> favoriteIds = favoriteRepository.findAllRecipeIdsByUserId(userId);

        recipes.sort(
                Comparator.comparing(Recipe::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder()))
        );

        // DTO 변환
        return recipes.stream()
                .map(r -> new RecipeListResponse(
                        r.getId(),
                        r.getTitle(),
                        favoriteIds.contains(r.getId())
                ))
                .toList();
    }

    @Transactional
    public RecipeDetailResponse replaceRecipe(Long recipeId, RecipeCreateRequest request, Long userId) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RestApiException(RecipeErrorCode.RECIPE_NOT_FOUND));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new RestApiException(RecipeErrorCode.RECIPE_FORBIDDEN);
        }

        //레시피(부모) 업데이트
        recipe.updateTitle(request.title());
        recipe.updateSteps(request.steps());
        recipe.updateUrl(request.url());

        recipeIngredientRepository.deleteAllByRecipeId(recipeId); //레시피 재료 전체 삭제

        //레시피 재료 재생성
        List<RecipeIngredient> children = new ArrayList<>();
        for (var i : request.ingredients()) {
            String ingredientName = (i.ingredientName() == null) ? null : i.ingredientName().trim();
            if (ingredientName == null || ingredientName.isEmpty()) {
                throw new RestApiException(IngredientErrorCode.NULL_INGREDIENT);
            }

            Ingredient ing = ingredientRepository.findByName(ingredientName)
                    .orElseGet(() -> ingredientRepository.save(
                            Ingredient.builder().ingredientName(ingredientName).build()
                    ));

            RecipeIngredient ri = RecipeIngredient.builder()
                    .recipe(recipe)
                    .ingredient(ing)
                    .quantity(i.quantity())
                    .unit(i.unit())
                    .build();

            children.add(ri);
        }
        recipeIngredientRepository.saveAll(children);

        return RecipeDetailResponse.from(recipe, recipeIngredientRepository.findAllByRecipeId(recipeId));
    }

    @Transactional
    public List<RecipeListResponse> deleteRecipes(List<Long> recipeIds, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        List<Recipe> toDelete = recipeRepository.findAllById(recipeIds);

        toDelete.removeIf(r -> r.getUser() == null || !r.getUser().getId().equals(user.getId()));


        if (!toDelete.isEmpty()) {
            List<Long> toDeleteRecipeIds = toDelete.stream().map(Recipe::getId).toList();
            recipeIngredientRepository.deleteAllByRecipe_IdIn(toDeleteRecipeIds);
            recipeIngredientRepository.flush();
            recipeRepository.deleteAllInBatch(toDelete);
        }

        return getRecipeList(user.getId());
    }

    @Transactional
    public boolean favoriteToggle(Long userId, Long recipeId) {
        if (favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId)) {
            favoriteRepository.deleteByUserIdAndRecipeId(userId, recipeId);
            return false;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.NULL_USER));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RestApiException(RecipeErrorCode.RECIPE_NOT_FOUND));
        favoriteRepository.save(FavoritesRecipe.of(user, recipe));
        return true;
    }

    @Transactional
    public boolean ShareToggle(Long refrigeratorId, Long recipeId) {
        Refrigerator refrigerator = refrigeratorRepository.findById(refrigeratorId)
                .orElseThrow(() -> new RestApiException(RefrigeratorErrorCode.NULL_REFRIGERATOR));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RestApiException(RecipeErrorCode.RECIPE_NOT_FOUND));

        if (shareRepository.existsByRefrigeratorIdAndRecipeId(refrigeratorId, recipeId)) {
            shareRepository.deleteByRefrigeratorIdAndRecipeId(refrigeratorId, recipeId);
            return false;
        }

        shareRepository.save(new Share(refrigerator, recipe));
        return true;
    }

    @Transactional
    public List<RecipeListResponse> listSharedRecipes(Long refrigeratorId, Long userId) {
        List<Recipe> recipes = shareRepository.findRecipesByRefrigeratorId(refrigeratorId);

        Set<Long> favIds = (userId == null) ? Set.of()
                : favoriteRepository.findAllRecipeIdsByUserId(userId);

        recipes.sort(
                Comparator.comparing(Recipe::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder()))
        );

        return recipes.stream()
                .map(r -> new RecipeListResponse(r.getId(), r.getTitle(), favIds.contains(r.getId())))
                .toList();
    }


    @Transactional
    public List<RecipeListResponse> listFavoriteRecipes(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        Set<Long> favoriteIds = favoriteRepository.findAllRecipeIdsByUserId(userId);
        if (favoriteIds.isEmpty()) {
            return List.of();
        }

        List<Recipe> recipes = recipeRepository.findAllById(favoriteIds);
        recipes.sort(
                Comparator.comparing(Recipe::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder()))
        );

        return recipes.stream()
                .map(r -> new RecipeListResponse(
                        r.getId(),
                        r.getTitle(),
                        true // 즐겨찾기 목록이므로 항상 true
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockResponse> getRefrigeratorIngredientStocks(Long refrigeratorId, List<String> ingredientNames) {
        //재고 조회
        List<RefrigeratorIngredient> stocks =
                refrigeratorIngredientRepository.findAllByRefrigeratorIdAndIngredientNames(refrigeratorId, ingredientNames);

        return StockResponse.fromRefrigeratorIngredient(stocks);
    }

    @Transactional
    public CookingResponse cookingUsage(Long userId, CookingRequest req) {
        Refrigerator ref = refrigeratorRepository.findById(req.refrigeratorId())
                .orElseThrow(() -> new RestApiException(RefrigeratorErrorCode.NULL_REFRIGERATOR));

        List<String> recipeIngredientNames = req.recipeIngredients().stream()
                .map(CookingRequest.Item::ingredientName)
                .toList();

        List<RefrigeratorIngredient> stocks =
                refrigeratorIngredientRepository.findAllByRefrigeratorIdAndIngredientNames(req.refrigeratorId(), recipeIngredientNames);

        Map<String, CookingRequest.Item> reqMap = req.recipeIngredients().stream()
                .collect(Collectors.toMap(
                        CookingRequest.Item::ingredientName,
                        it -> it,
                        (a, b) -> b
                ));

        //차감 로직
        List<RefrigeratorIngredient> remainStocks = stocks.stream()
                .peek(stock -> {
                    String name = stock.getIngredient().getName();
                    CookingRequest.Item item = reqMap.get(name);

                    BigDecimal before = BigDecimal.valueOf(stock.getQuantity())
                            .setScale(1, RoundingMode.HALF_UP);

                    // 차감량 = 전체수량 × percent / 100
                    BigDecimal used = BigDecimal.valueOf(item.quantity())
                            .multiply(BigDecimal.valueOf(item.percent()))
                            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                            .setScale(1, RoundingMode.HALF_UP);

                    BigDecimal after = before.subtract(used);
                    if (after.compareTo(BigDecimal.ZERO) < 0) after = BigDecimal.ZERO;

                    stock.updateQuantity(after.doubleValue());
                })
                .toList();

        refrigeratorIngredientRepository.saveAll(remainStocks);

        List<CookingResponse.ResultItem> result = remainStocks.stream()
                .map(ri -> new CookingResponse.ResultItem(
                        ri.getIngredient().getId(),
                        ri.getIngredient().getName(),
                        ri.getQuantity()
                ))
                .toList();


        //사용 기록 저장
        List<History> histories = remainStocks.stream()
                .map(ri -> {
                    String name = ri.getIngredient().getName();
                    CookingRequest.Item item = reqMap.get(name);

                    BigDecimal used = BigDecimal.valueOf(item.quantity())
                            .multiply(BigDecimal.valueOf(item.percent()))
                            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                            .setScale(1, RoundingMode.HALF_UP);

                    return new History(
                            userRepository.getReferenceById(userId),
                            ri,
                            used.doubleValue()
                    );
                })
                .toList();

        historyRepository.saveAll(histories);


        return new CookingResponse(req.refrigeratorId(), userId, result);
    }

    @Transactional(readOnly = true)
    public List<RecipeListResponse> search(String keyword, Long userId) {
        String word = (keyword == null) ? "" : keyword.trim();
        if (word.isEmpty()) {
            return List.of();
        }

        List<Recipe> recipes = recipeRepository.searchAll(word);

        Set<Long> favIds = (userId == null)
                ? Set.of()
                : favoriteRepository.findAllRecipeIdsByUserId(userId);

        return recipes.stream()
                .map(r -> new RecipeListResponse(
                        r.getId(),
                        r.getTitle(),
                        favIds.contains(r.getId())
                ))
                .toList();
    }

}
