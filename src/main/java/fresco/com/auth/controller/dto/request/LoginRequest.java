package fresco.com.auth.controller.dto.request;

import fresco.com.auth.domain.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "null이면 안됩니다.")
        Provider provider,
        @NotBlank(message = "공백이면 안됩니다.")
        String accessToken
) {
}