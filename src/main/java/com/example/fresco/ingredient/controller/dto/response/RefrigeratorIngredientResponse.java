package com.example.fresco.ingredient.controller.dto.response;

import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;

import java.time.LocalDate;
import java.util.List;

public record RefrigeratorIngredientResponse(
        Long id,
        Short ingredientId,
        Short categoryId,
        String ingredientName,
        LocalDate expirationDate,
        Double quantity
) {
    public static RefrigeratorIngredientResponse from(RefrigeratorIngredient refrigeratorIngredient) {
        return new RefrigeratorIngredientResponse(
                refrigeratorIngredient.getId(),
                refrigeratorIngredient.getIngredient().getId(),
                refrigeratorIngredient.getCategory().getId(),
                refrigeratorIngredient.getIngredient().getName(),
                refrigeratorIngredient.getExpirationDate(),
                refrigeratorIngredient.getQuantity()
        );
    }

    public static List<RefrigeratorIngredientResponse> getListFromRefrigeratorIngredients(List<RefrigeratorIngredient> refrigeratorIngredients) {
        return refrigeratorIngredients.stream().map(RefrigeratorIngredientResponse::from).toList();
    }
}
