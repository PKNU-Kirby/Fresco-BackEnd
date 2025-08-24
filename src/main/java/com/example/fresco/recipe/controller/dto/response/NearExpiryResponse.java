package com.example.fresco.recipe.controller.dto.response;

import java.util.List;

public record NearExpiryResponse(
        Long refrigeratorId,
        List<String> day1,
        List<String> day2,
        List<String> day3
) {
}