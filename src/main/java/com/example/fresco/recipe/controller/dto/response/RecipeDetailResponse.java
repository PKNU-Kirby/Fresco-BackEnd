package com.example.fresco.recipe.controller.dto.response;

import com.example.fresco.recipe.domain.Recipe;
import com.example.fresco.recipe.domain.RecipeIngredient;

import java.util.List;

public record RecipeDetailResponse(
        Long recipeId,
        String title,
        String steps,
        String url,
        List<RecipeIngredientResponse> ingredients
) {
    public static RecipeDetailResponse from(Recipe recipe, List<RecipeIngredient> items) {
        List<RecipeIngredientResponse> ingredientDtos = items.stream()
                .map(RecipeIngredientResponse::from)
                .toList();

        return new RecipeDetailResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getSteps(),
                recipe.getUrl(),
                ingredientDtos
        );
    }
}
