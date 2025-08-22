package com.example.fresco.recipe.controller.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CookingResponse (
        Long refrigeratorId,
        Long userId,
        List<ResultItem> results
) {
    public record ResultItem(
            Short ingredientId,
            String name,
            Double quantity
    ) {
    }
}
