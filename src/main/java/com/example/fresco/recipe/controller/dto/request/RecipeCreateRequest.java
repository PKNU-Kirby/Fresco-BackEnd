package com.example.fresco.recipe.controller.dto.request;

import java.util.List;

public record RecipeCreateRequest(
        String title,
        List<RecipeIngredients> ingredients,
        String steps,
        String url
){
        public record RecipeIngredients (
                String ingredientName,
                Integer quantity,
                String scale
        ){}
}
