package com.example.fresco.auth.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "빈 문자열이면 안됩니다.")
        String refreshToken
) {
}