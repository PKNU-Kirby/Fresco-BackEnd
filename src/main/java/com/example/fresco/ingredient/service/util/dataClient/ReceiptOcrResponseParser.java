package com.example.fresco.ingredient.service.util.dataClient;

import com.example.fresco.ingredient.controller.dto.response.ocr.ImageResult;
import com.example.fresco.ingredient.controller.dto.response.ocr.Item;
import com.example.fresco.ingredient.controller.dto.response.ocr.ReceiptResponse;
import com.example.fresco.ingredient.controller.dto.response.ocr.SubResult;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ReceiptOcrResponseParser {

    public List<String> parseReceiptResponse(ReceiptResponse response) {
        if (response == null || response.images() == null) {
            return Collections.emptyList();
        }

        return response.images().stream()
                .filter(Objects::nonNull)
                .flatMap(image -> parseImageResult(image))
                .collect(Collectors.toList());
    }

    private Stream<String> parseImageResult(ImageResult image) {
        if (image.receipt() == null ||
                image.receipt().result() == null ||
                image.receipt().result().subResults() == null) {
            return Stream.empty();
        }

        return image.receipt().result().subResults().stream()
                .filter(Objects::nonNull)
                .flatMap(this::parseSubResult);
    }

    private Stream<String> parseSubResult(SubResult subResult) {
        if (subResult.items() == null) {
            return Stream.empty();
        }

        return subResult.items().stream()
                .filter(Objects::nonNull)
                .map(this::createProductName)
                .filter(Objects::nonNull);
    }

    private String createProductName(Item item) {
        try {
            String name = item.name().text();

            if (isValidFoodInfo(name)) {
                return name;
            }
            return null;
        } catch (Exception e) {
            // 로그 남기고 null 반환
            return null;
        }
    }

    private boolean isValidFoodInfo(String name) {
        return name != null && !name.trim().isEmpty();
    }
}

