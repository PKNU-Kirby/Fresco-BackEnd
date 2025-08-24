package com.example.fresco.ingredient.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ReceiptMatchListResponse(
        List<ReceiptMatchResponse> receiptMatchList
) {
    public List<Short> getIngredientIds() {
        return receiptMatchList.stream().map(matchInfo -> matchInfo.ingredientId).toList();
    }

    public record ReceiptMatchResponse(
            @JsonProperty("input_name")
            String inputName,
            Short ingredientId,
            String ingredientName,
            Short categoryId,
            String categoryName
    ) {
    }
}