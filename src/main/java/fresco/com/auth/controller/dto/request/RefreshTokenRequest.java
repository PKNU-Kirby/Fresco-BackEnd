package fresco.com.auth.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "공백이면 안됩니다.")
        String refreshToken
) {
}