package fresco.com.refrigerator.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeleteRefrigeratorRequest(
        @NotNull
        Long refrigeratorId
) {
}
