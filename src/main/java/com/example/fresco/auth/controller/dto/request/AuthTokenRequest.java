package com.example.fresco.auth.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthTokenRequest(
        @NotBlank(message = "빈 문자열이면 안 됩니다.")
        String accessToken,
        @NotBlank(message = "빈 문자열이면 안 됩니다.")
        String refreshToken
) {
}
