package fresco.com.auth.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthTokenRequest(
        @NotBlank(message = "액세스 토큰은 비어있으면 안 됩니다.")
        String accessToken,
        @NotBlank(message = "리프레시 토큰은 비어있으면 안 됩니다.")
        String refreshToken
) {
}
