package fresco.com.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record AuthTokenRequest(
        @NotNull
        String accessToken,
        @NotNull
        String refreshToken
) {
}
