package com.example.fresco.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecipeErrorCode implements ErrorCode {
    NULL_RECIPE_RECOMMEND("RECIPE_ERR_001", HttpStatus.BAD_REQUEST, "레시피 생성에 실패했습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}