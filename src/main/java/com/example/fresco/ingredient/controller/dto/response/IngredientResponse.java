package com.example.fresco.ingredient.controller.dto.response;

import com.example.fresco.ingredient.domain.Ingredient;

import java.util.Date;

public record IngredientResponse(
        Long id,
        Long categoryId,
        String name,
        Date expirationDate,
        Integer quantity
) {
    public static IngredientResponse from(Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getCategory().getId(),
                ingredient.getName(),
                ingredient.getExpirationDate(),
                ingredient.getQuantity()
        );
    }
}
