package com.example.fresco.refrigerator.controller.dto.request;

public record UpdateRefrigeratorRequest(
        Long refrigeratorId,
        String name
) {
}
