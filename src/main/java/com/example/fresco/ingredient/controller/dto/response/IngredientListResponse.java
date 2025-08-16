package com.example.fresco.ingredient.controller.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record IngredientListResponse(
        List<IngredientResponse> imageList
) {
    public static List<IngredientResponse> getIngredientResponseWithExpirationDate(
            List<IngredientResponse> ingredientList, Map<Long, LocalDate> expirationDateMap) {
        return ingredientList.stream()
                .map(ingredient -> new IngredientResponse(
                        ingredient.ingredientId(),
                        ingredient.ingredientName(),
                        ingredient.categoryId(),
                        ingredient.categoryName(),
                        expirationDateMap.get(ingredient.ingredientId())
                ))
                .toList();
    }

    public List<Long> getIngredientIds() {
        return imageList.stream().map(IngredientResponse::ingredientId).toList();
    }
}