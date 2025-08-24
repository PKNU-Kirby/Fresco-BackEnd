package com.example.fresco.ingredient.controller.dto.response;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record AutoCompleteSearchResponse(
        Long ingredientId,
        String ingredientName,
        Long categoryId,
        String categoryName
) {
    public static List<AutoCompleteSearchResponse> convertToAutoCompleteSearchResponse(List<Object[]> similarMatches) {
        List<AutoCompleteSearchResponse> result = new ArrayList<>();
        for (Object[] match : similarMatches) {
            result.add(AutoCompleteSearchResponse.builder()
                    .ingredientId((Long) match[0])
                    .ingredientName((String) match[1])
                    .categoryId((Long) match[2])
                    .categoryName((String) match[3])
                    .build()
            );
        }
        return result;
    }
}