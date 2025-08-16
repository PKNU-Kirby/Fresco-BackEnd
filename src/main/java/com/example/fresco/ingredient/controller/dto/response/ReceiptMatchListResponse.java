package com.example.fresco.ingredient.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ReceiptMatchListResponse(
        List<ReceiptMatchResponse> receiptMatchList
) {
    public List<Long> getIngredientIds() {
        return receiptMatchList.stream().map(matchInfo -> matchInfo.ingredientId).toList();
    }

    public record ReceiptMatchResponse(
            @JsonProperty("input_name")
            String inputName,
            Long ingredientId,
            String ingredientName,
            Long categoryId,
            String categoryName
    ) {
    }
}