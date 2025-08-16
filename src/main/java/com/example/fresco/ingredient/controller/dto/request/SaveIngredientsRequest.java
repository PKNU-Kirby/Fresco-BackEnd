package com.example.fresco.ingredient.controller.dto.request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record SaveIngredientsRequest(
        List<SaveIngredientInfo> ingredientsInfo
) {
    public List<Long> getIngredientIds() {
        List<Long> ingredientIds = new ArrayList<>();
        for (SaveIngredientInfo ingredientInfo : ingredientsInfo) {
            ingredientIds.add(ingredientInfo.ingredientId);
        }
        return ingredientIds;
    }

    public record SaveIngredientInfo(
            Long ingredientId,
            Long categoryId,
            LocalDate expirationDate
    ) {
    }
}