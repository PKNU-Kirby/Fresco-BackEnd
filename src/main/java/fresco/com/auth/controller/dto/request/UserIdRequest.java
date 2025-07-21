package fresco.com.auth.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserIdRequest(
        @NotNull(message = "Null이면 안됩니다.")
        Long userId
) {
}
