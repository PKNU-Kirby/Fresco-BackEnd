package com.example.fresco.ingredient.controller.dto.response;

import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;

import java.time.LocalDate;
import java.util.List;

public record IngredientResponse(
        Long ingredientId,
        String ingredientName,
        Long categoryId,
        String categoryName,
        LocalDate expirationDate
) {
    public static IngredientResponse from(RefrigeratorIngredient refrigeratorIngredient) {
        return new IngredientResponse(
                refrigeratorIngredient.getIngredient().getId(),
                refrigeratorIngredient.getIngredient().getName(),
                refrigeratorIngredient.getCategory().getId(),
                refrigeratorIngredient.getCategory().getName(),
                refrigeratorIngredient.getExpirationDate()
        );
    }

    public static List<IngredientResponse> getListFromRefrigeratorIngredients(List<RefrigeratorIngredient> refrigeratorIngredients) {
        return refrigeratorIngredients.stream().map(IngredientResponse::from).toList();
    }
}
