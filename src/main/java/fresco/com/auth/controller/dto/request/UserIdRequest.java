package fresco.com.auth.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserIdRequest(
        @NotNull(message = "유저 ID는 Null이면 안됩니다.")
        Long userId
) {
}
