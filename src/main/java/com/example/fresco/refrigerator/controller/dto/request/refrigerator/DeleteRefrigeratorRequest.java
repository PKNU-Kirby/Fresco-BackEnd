package com.example.fresco.refrigerator.controller.dto.request.refrigerator;

import jakarta.validation.constraints.NotNull;

public record DeleteRefrigeratorRequest(
        @NotNull
        Long refrigeratorId
) {
}
