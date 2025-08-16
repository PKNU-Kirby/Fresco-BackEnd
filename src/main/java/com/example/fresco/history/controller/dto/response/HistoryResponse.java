package com.example.fresco.history.controller.dto.response;

import com.example.fresco.history.domain.History;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HistoryResponse(
        Long consumerId,
        String consumerName,
        Long ingredientId,
        String ingredientName,
        LocalDateTime usedAt
) {
    public static HistoryResponse from(History history) {
        return HistoryResponse.builder()
                .consumerId(history.getUser().getId())
                .consumerName(history.getUser().getName())
                .ingredientId(history.getRefrigeratorIngredient().getIngredient().getId())
                .ingredientName(history.getRefrigeratorIngredient().getIngredient().getName())
                .usedAt(history.getCreatedDate())
                .build();
    }
}