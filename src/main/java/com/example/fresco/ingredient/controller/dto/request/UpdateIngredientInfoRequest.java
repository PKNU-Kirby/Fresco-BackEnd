package com.example.fresco.ingredient.controller.dto.request;

import java.time.LocalDate;

public record UpdateIngredientInfoRequest(
        String unit,
        LocalDate expirationDate,
        Double quantity
) {
    public UpdateIngredientConditionCommand toCommand(Long userId, Long refrigeratorIngredientId) {
        return new UpdateIngredientConditionCommand(
                userId,
                refrigeratorIngredientId,
                quantity,
                unit,
                expirationDate
        );
    }
}