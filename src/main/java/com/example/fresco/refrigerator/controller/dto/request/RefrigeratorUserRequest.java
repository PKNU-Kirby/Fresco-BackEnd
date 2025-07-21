package com.example.fresco.refrigerator.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record RefrigeratorUserRequest(
        @NotNull
        Long userId,
        @NotNull
        Long refrigeratorId) {
}
