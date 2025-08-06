package com.example.fresco.recipe.controller.dto.response;

import com.example.fresco.recipe.domain.Recipe;

import java.util.List;

public record RecipeDetailResponse(
        Long recipeId,
        String title,
        String steps,
        String url,
        List<RecipeIngredientResponse> ingredients
) {
    public static RecipeDetailResponse from(Recipe recipe) {
        return new RecipeDetailResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getSteps(),
                recipe.getUrl(),
                recipe.getIngredients().stream()
                        .map(RecipeIngredientResponse::from)
                        .toList()
        );
    }
}
