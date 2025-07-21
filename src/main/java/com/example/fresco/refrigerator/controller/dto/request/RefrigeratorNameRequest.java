package com.example.fresco.refrigerator.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefrigeratorNameRequest(
        @NotBlank(message = "냉장고 이름은 빈 문자열이면 안됩니다.")
        String name
) {
}
