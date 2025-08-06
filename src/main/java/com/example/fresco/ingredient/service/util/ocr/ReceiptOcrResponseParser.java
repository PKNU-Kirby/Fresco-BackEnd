package com.example.fresco.ingredient.service.util.ocr;

import com.example.fresco.ingredient.controller.dto.response.ocr.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ReceiptOcrResponseParser {

    public List<FoodPair> parseReceiptResponse(ReceiptResponse response) {
        if (response == null || response.images() == null) {
            return Collections.emptyList();
        }

        return response.images().stream()
                .filter(Objects::nonNull)
                .flatMap(image -> parseImageResult(image))
                .collect(Collectors.toList());
    }

    private Stream<FoodPair> parseImageResult(ImageResult image) {
        if (image.receipt() == null ||
                image.receipt().result() == null ||
                image.receipt().result().subResults() == null) {
            return Stream.empty();
        }

        return image.receipt().result().subResults().stream()
                .filter(Objects::nonNull)
                .flatMap(this::parseSubResult);
    }

    private Stream<FoodPair> parseSubResult(SubResult subResult) {
        if (subResult.items() == null) {
            return Stream.empty();
        }

        return subResult.items().stream()
                .filter(Objects::nonNull)
                .map(this::createFoodPair)
                .filter(Objects::nonNull);
    }

    private FoodPair createFoodPair(Item item) {
        try {
            String name = item.name().text();
            Integer count = Integer.parseInt(item.count().text());

            if (isValidFoodInfo(name, count)) {
                return new FoodPair(name, count);
            }
            return null;
        } catch (Exception e) {
            // 로그 남기고 null 반환
            return null;
        }
    }

    private boolean isValidFoodInfo(String name, Integer count) {
        return name != null && !name.trim().isEmpty() &&
                count != null && count > 0;
    }
}

