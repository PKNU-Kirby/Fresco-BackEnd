package com.example.fresco.recipe.controller.dto.response;

import com.example.fresco.recipe.domain.RecipeIngredient;

public record RecipeIngredientResponse(
        Long recipeIngredientId,
        String name,
        Double quantity,
        String unit,
        String instead
) {
    public static RecipeIngredientResponse from(RecipeIngredient ri) {
        return new RecipeIngredientResponse(
                ri.getId(),
                ri.getIngredient().getName(),
                ri.getQuantity(),
                ri.getUnit(),
                ri.getInstead()
        );
    }
}
