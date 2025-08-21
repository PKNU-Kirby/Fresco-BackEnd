package com.example.fresco.recipe.controller.dto.request;

import java.util.List;

public record StockRequest(
        List<String> recipeIngredientNames
) {
}
