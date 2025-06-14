package fresco.com.auth.controller.dto.request;

import fresco.com.auth.domain.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "provider는 null이면 안됩니다.")
        Provider provider,
        @NotBlank(message = "accessToken은 공백이면 안됩니다.")
        String accessToken
) {
}