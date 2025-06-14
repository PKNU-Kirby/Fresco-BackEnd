package fresco.com.auth.controller.dto.response;

import jakarta.validation.constraints.NotBlank;

public record LoginSuccessResponse(
        @NotBlank(message = "공백이면 안 됩니다.")
        String accessToken,
        @NotBlank(message = "공백이면 안 됩니다.")
        String refreshToken
) {
}
