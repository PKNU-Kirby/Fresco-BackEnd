package com.example.fresco.ingredient.controller.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record IngredientListResponse(
        List<RefrigeratorIngredientResponse> imageList
) {
    public static List<RefrigeratorIngredientResponse> getIngredientResponseWithExpirationDate(
            List<RefrigeratorIngredientResponse> ingredientList, Map<Short, LocalDate> expirationDateMap) {
        return ingredientList.stream()
                .map(ingredient -> new RefrigeratorIngredientResponse(
                        ingredient.id(),
                        ingredient.ingredientId(),
                        ingredient.categoryId(),
                        ingredient.ingredientName(),
                        expirationDateMap.get(ingredient.ingredientId()),
                        ingredient.quantity()
                ))
                .toList();
    }

    public List<Short> getIngredientIds() {
        return imageList.stream().map(RefrigeratorIngredientResponse::ingredientId).toList();
    }
}
