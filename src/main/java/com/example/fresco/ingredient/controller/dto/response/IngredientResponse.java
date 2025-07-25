package com.example.fresco.ingredient.controller.dto.response;

import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;

import java.time.LocalDate;

public record IngredientResponse(
        Long ingredientId,
        Long categoryId,
        String name,
        LocalDate expirationDate,
        Integer quantity
) {
    public static IngredientResponse from(RefrigeratorIngredient ingredient) {
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getCategory().getId(),
                ingredient.getName(),
                ingredient.getExpirationDate(),
                ingredient.getQuantity()
        );
    }
}
