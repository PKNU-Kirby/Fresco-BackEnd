package com.example.fresco.ingredient.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

import java.time.LocalDate;

public record UpdateIngredientConditionCommand(
        @NotNull
        Long userId,
        @NonNull
        Long refrigeratorIngredientId,
        Double quantity,
        String unit,
        LocalDate expirationDate
) {
}
