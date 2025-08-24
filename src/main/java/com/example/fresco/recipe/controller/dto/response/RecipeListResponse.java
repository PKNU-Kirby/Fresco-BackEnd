package com.example.fresco.recipe.controller.dto.response;

public record RecipeListResponse(
        Long recipeId,
        String title,
        boolean favorite
) {
}
