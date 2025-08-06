package com.example.fresco.recipe.controller.dto.response;

import com.example.fresco.recipe.domain.RecipeIngredient;

public record RecipeIngredientResponse(
        Long ingredientId,
        String name,
        Integer quantity,
        String instead
) {
    public static RecipeIngredientResponse from(RecipeIngredient ri) {
        return new RecipeIngredientResponse(
                ri.getId(),
                ri.getIngredient().getName(),
                ri.getQuantity(),
                ri.getInstead()
        );
    }
}
