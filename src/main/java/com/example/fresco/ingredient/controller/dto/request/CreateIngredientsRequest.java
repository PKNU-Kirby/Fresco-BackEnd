package com.example.fresco.ingredient.controller.dto.request;

import java.time.LocalDate;
import java.util.List;

public record CreateIngredientsRequest(
        List<CreateIngredientInfo> ingredientsInfo
        ) {
    public record CreateIngredientInfo(
            String name,
            Integer quantity,
            LocalDate expirationDate,
            Long categoryId
    ) {
    }
}
