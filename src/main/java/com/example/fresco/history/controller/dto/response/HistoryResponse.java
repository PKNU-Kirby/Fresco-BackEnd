package com.example.fresco.history.controller.dto.response;

import com.example.fresco.history.domain.History;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HistoryResponse(
        Long consumerId,
        String consumerName,
        Long refrigeratorIngredientId,
        String ingredientName,
        Double usedQuantity,
        String unit,
        LocalDateTime usedAt
) {
    public static HistoryResponse from(History history) {
        return HistoryResponse.builder()
                .consumerId(history.getUser().getId())
                .consumerName(history.getUser().getName())
                .refrigeratorIngredientId(history.getRefrigeratorIngredient().getId())
                .ingredientName(history.getRefrigeratorIngredient().getIngredient().getName())
                .usedQuantity(history.getUsedQuantity())
                .unit(history.getRefrigeratorIngredient().getUnit())
                .usedAt(history.getCreatedDate())
                .build();
    }
}