package com.example.fresco.recipe.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.AuthErrorCode;
import com.example.fresco.global.response.error.IngredientErrorCode;
import com.example.fresco.global.response.error.RecipeErrorCode;
import com.example.fresco.ingredient.domain.Ingredient;
import com.example.fresco.ingredient.domain.repository.IngredientRepository;
import com.example.fresco.recipe.controller.dto.request.RecipeCreateRequest;
import com.example.fresco.recipe.controller.dto.response.OpenAiResponse;
import com.example.fresco.recipe.controller.dto.response.RecipeDetailResponse;
import com.example.fresco.recipe.controller.dto.response.RecipeListResponse;
import com.example.fresco.recipe.domain.*;
import com.example.fresco.recipe.domain.Repository.FavoriteRepository;
import com.example.fresco.recipe.domain.Repository.RecipeRepository;
import com.example.fresco.recipe.domain.Repository.ShareRepository;
import com.example.fresco.recipe.util.OpenAIClient;
import com.example.fresco.refrigerator.domain.Refrigerator;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final FavoriteRepository favoriteRepository;
    private final ShareRepository shareRepository;
    private final RefrigeratorRepository refrigeratorRepository;

    @Transactional
    public OpenAiResponse generateRecipe(String prompt) {
        try {
            String argumentsJson = openAIClient.callFunction(prompt);
            return objectMapper.readValue(argumentsJson, OpenAiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            throw new RestApiException(RecipeErrorCode.NULL_RECIPE_RECOMMEND);
        }
    }

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
                                    .build()
                    ));


            RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .recipe(recipe)
                    .ingredient(ingredient)
                    .quantity(ingredientDto.quantity())
                    .instead(ingredientDto.scale())
                    .build();

            recipeIngredients.add(recipeIngredient);
        }

        recipe.getIngredients().addAll(recipeIngredients);
        Recipe saved = recipeRepository.save(recipe);

        return RecipeDetailResponse.from(saved);
    }

    @Transactional
    public RecipeDetailResponse getRecipeDetail(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RestApiException(RecipeErrorCode.NULL_RECIPE));
        return RecipeDetailResponse.from(recipe);
    }

    @Transactional
    public List<RecipeListResponse> getRecipeList(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        List<Recipe> recipes = recipeRepository.findAll();

        Set<Long> favoriteIds = favoriteRepository.findAllRecipeIdsByUserId(userId);

        recipes.sort(
                Comparator
                        .comparing((Recipe r) -> !favoriteIds.contains(r.getId())) // false(즐겨찾기) 먼저
                        .thenComparing(Recipe::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder()))
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
    public RecipeDetailResponse replaceRecipe(Long recipeId,RecipeCreateRequest request, Long userId){
        Recipe recipe = recipeRepository.getReferenceById(recipeId);

        recipe.updateTitle(request.title());
        recipe.updateSteps(request.steps());
        recipe.updateUrl(request.url());

        recipe.getIngredients().clear();
        for (var i : request.ingredients()) {
            Ingredient ing = ingredientRepository.findByName(i.ingredientName())
                    .orElseGet(() -> ingredientRepository.save(
                            Ingredient.builder().ingredientName(i.ingredientName()).build()
                    ));

            RecipeIngredient ri = RecipeIngredient.builder()
                    .recipe(recipe)
                    .ingredient(ing)
                    .quantity(i.quantity())
                    .instead(i.scale())
                    .build();

            recipe.getIngredients().add(ri);
        }

        return RecipeDetailResponse.from(recipeRepository.save(recipe));
    }

    @Transactional
    public List<RecipeListResponse> deleteRecipes(List<Long> recipeIds, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        List<Recipe> toDelete = recipeRepository.findAllById(recipeIds);

        toDelete.removeIf(r -> r.getUser() == null || !r.getUser().getId().equals(user.getId()));

        if (!toDelete.isEmpty()) {
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

        User user = userRepository.getReferenceById(userId);
        Recipe recipe = recipeRepository.getReferenceById(recipeId);
        favoriteRepository.save(FavoritesRecipe.of(user, recipe));
        return true;
    }

    @Transactional
    public boolean ShareToggle(Long refrigeratorId, Long recipeId) {
        if (shareRepository.existsByRefrigeratorIdAndRecipeId(refrigeratorId, recipeId)) {
            shareRepository.deleteByRefrigeratorIdAndRecipeId(refrigeratorId, recipeId);
            return false;
        }
        Refrigerator refrigerator = refrigeratorRepository.getReferenceById(refrigeratorId);
        Recipe recipe = recipeRepository.getReferenceById(recipeId);

        shareRepository.save(Share.of(refrigerator, recipe));
        return true;
    }

    @Transactional
    public List<RecipeListResponse> listSharedRecipes(Long refrigeratorId, Long userId) {
        List<Recipe> recipes = shareRepository.findRecipesByRefrigeratorId(refrigeratorId);

        Set<Long> favIds = (userId == null) ? Set.of()
                : favoriteRepository.findAllRecipeIdsByUserId(userId);

        recipes.sort(
                Comparator.comparing((Recipe r) -> !favIds.contains(r.getId()))
                        .thenComparing(Recipe::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder()))
        );

        return recipes.stream()
                .map(r -> new RecipeListResponse(r.getId(), r.getTitle(), favIds.contains(r.getId())))
                .toList();
    }



}
