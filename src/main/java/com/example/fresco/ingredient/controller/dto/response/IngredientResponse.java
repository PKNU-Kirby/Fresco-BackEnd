package com.example.fresco.ingredient.controller.dto.response;

import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;

import java.time.LocalDate;

public record IngredientResponse(
        Long id,
        Short ingredientId,
        Short categoryId,
        String name,
        LocalDate expirationDate,
        Integer quantity
) {
    public static IngredientResponse from(RefrigeratorIngredient refrigeratorIngredient) {
        return new IngredientResponse(
                refrigeratorIngredient.getId(),
                refrigeratorIngredient.getIngredient().getId(),
                refrigeratorIngredient.getCategory().getId(),
                refrigeratorIngredient.getIngredient().getName(),
                refrigeratorIngredient.getExpirationDate(),
                refrigeratorIngredient.getQuantity()
        );
    }
}
