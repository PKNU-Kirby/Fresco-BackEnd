package com.example.fresco.ingredient.controller.dto.request;

import java.time.LocalDate;

public record UpdateIngredientInfoRequest(
        String name,
        LocalDate expirationDate,
        Integer quantity
) {
    public UpdateIngredientConditionCommand toCommand(Long refrigeratorIngredientId) {
        return new UpdateIngredientConditionCommand(
                refrigeratorIngredientId,
                name,
                expirationDate,
                quantity
        );
    }
}