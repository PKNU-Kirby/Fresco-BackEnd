package fresco.com.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserDetailRequest(
        @NotNull
        Long userId
) {
}
