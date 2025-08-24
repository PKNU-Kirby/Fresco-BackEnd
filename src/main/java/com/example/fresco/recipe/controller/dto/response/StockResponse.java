package com.example.fresco.recipe.controller.dto.response;

import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;

import java.util.List;

public record StockResponse(
        Long refrigeratorIngredientId,
        String ingredientName,
        Double quantity,
        String unit
) {
    public static List<StockResponse> fromRefrigeratorIngredient(List<RefrigeratorIngredient> stocks) {
        return stocks.stream()
                .map(ri -> new StockResponse(
                        ri.getId(),
                        ri.getIngredient().getName(),
                        ri.getQuantity(),
                        ri.getUnit()
                ))
                .toList();
    }
}