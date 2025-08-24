package com.example.fresco.ingredient.controller.dto.response;

import java.util.ArrayList;
import java.util.List;

public record AutoCompleteSearchResponse(
        Short ingredientId,
        String ingredientName,
        Short categoryId,
        String categoryName
) {
    public static List<AutoCompleteSearchResponse> convertToAutoCompleteSearchResponse(List<Object[]> similarMatches) {
        List<AutoCompleteSearchResponse> result = new ArrayList<>();
        for (Object[] match : similarMatches) {
            result.add(new AutoCompleteSearchResponse(
                    (Short) match[0],
                    (String) match[1],
                    (Short) match[2],
                    (String) match[3]
            ));
        }
        return result;
    }
}