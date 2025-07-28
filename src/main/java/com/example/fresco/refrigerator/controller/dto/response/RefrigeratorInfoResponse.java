package com.example.fresco.refrigerator.controller.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefrigeratorInfoResponse(
        @NotNull(message = "냉장고 id는 null이면 안됩니다.")
        Long id,
        @NotBlank(message = "냉장고 이름은 빈 문자열이면 안됩니다.")
        String name,
        @NotNull(message = "장바구니 id는 null이면 안됩니다.")
        Long groceryListId
) {
}