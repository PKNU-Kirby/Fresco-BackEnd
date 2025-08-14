package com.example.fresco.ingredient.controller.dto.response;

import com.example.fresco.ingredient.controller.dto.response.ocr.FoodPair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ReceiptMatchResponse(
        List<IngredientMatchInfo> receiptMatches
) {
    public ReceiptMatchResponse defineQuantity(List<FoodPair> foodPairs) {
        Map<String, Integer> foodValueMap = foodPairs.stream()
                .collect(Collectors.toMap(
                        FoodPair::food,
                        FoodPair::value,
                        (existing, replacement) -> existing
                ));

        receiptMatches.forEach(matchInfo -> {
            Integer foundValue = foodValueMap.get(matchInfo.getInputName());
            if (foundValue != null) {
                matchInfo.setValue(foundValue);
            }
        });

        return this;
    }

    public List<String> getCategoryNames() {
        return receiptMatches.stream()
                .map(IngredientMatchInfo::getCategoryName)
                .toList();
    }
}