package com.example.fresco.recipe.controller.dto.request;

import java.util.List;

public record CookingRequest (
        Long refrigeratorId,
        List<Item> recipeIngredients
){
    public record Item(String ingredientName, Double quantity, String unit, Integer percent) {

    }
}
