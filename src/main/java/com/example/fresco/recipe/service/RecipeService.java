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
import com.example.fresco.recipe.domain.*;
import com.example.fresco.recipe.util.OpenAIClient;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final RecipeDetailRepository recipeDetailRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;

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
            Ingredient ingredient = ingredientRepository.findByName(ingredientDto.ingredientName())
                    .orElseThrow(() -> new RestApiException(IngredientErrorCode.NULL_INGREDIENT));

            RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .recipe(recipe)
                    .ingredient(ingredient)
                    .quantity(ingredientDto.quantity())
                    .instead(ingredientDto.scale())
                    .build();

            recipeIngredients.add(recipeIngredient);
        }

        recipe.getIngredients().addAll(recipeIngredients);
        Recipe saved = recipeDetailRepository.save(recipe);

        return RecipeDetailResponse.from(saved);
    }
}
