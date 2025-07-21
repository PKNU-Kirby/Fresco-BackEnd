package com.example.fresco.refrigerator.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record GetAllRefrigeratorRequest(
        @NotNull(message = "유저 ID는 null이면 안됩니다.")
        Long userId
) {
}
