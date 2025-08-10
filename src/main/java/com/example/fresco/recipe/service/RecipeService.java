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
import com.example.fresco.recipe.util.OpenAIClient;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));
        List<Recipe> recipes = recipeRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));

        List<Long> recipeIds = recipes.stream().map(Recipe::getId).toList();
        Set<Long> favoriteRecipeIds = favoriteRepository
                .findRecipeIdsByUserIdAndRecipeIds(user.getId(), recipeIds);


        return recipes.stream()
                .map(recipe -> new RecipeListResponse(
                        recipe.getId(),
                        recipe.getTitle(),
                        favoriteRecipeIds.contains(recipe.getId())
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
}
