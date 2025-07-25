package com.example.fresco.ingredient.controller.dto.request;

import lombok.NonNull;

import java.time.LocalDate;

public record UpdateIngredientConditionCommand(
        @NonNull
        Long refrigeratorIngredientId,
        String name,
        LocalDate expirationDate,
        Integer quantity
) {
}
