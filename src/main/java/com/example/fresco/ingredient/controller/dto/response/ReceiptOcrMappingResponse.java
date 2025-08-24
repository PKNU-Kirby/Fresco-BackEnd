package com.example.fresco.ingredient.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ReceiptOcrMappingResponse(
        @JsonProperty("input_name")
        String inputName,
        Short ingredientId,
        String ingredientName,
        Short categoryId,
        String categoryName,
        LocalDate expirationDate
) {
    public static ReceiptOcrMappingResponse from(ReceiptMatchListResponse.ReceiptMatchResponse matchList, LocalDate expirationDate) {
        return new ReceiptOcrMappingResponse(
                matchList.inputName(),
                matchList.ingredientId(),
                matchList.ingredientName(),
                matchList.categoryId(),
                matchList.categoryName(),
                expirationDate
        );
    }
}
