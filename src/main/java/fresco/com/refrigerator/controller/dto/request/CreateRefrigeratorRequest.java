package fresco.com.refrigerator.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRefrigeratorRequest(
        @NotNull(message = "유저 id는 null일 수 없습니다.")
        Long userId,
        @NotBlank(message = "냉장고 이름은 빈 문자열일 수 없습니다.")
        String name
) {
}