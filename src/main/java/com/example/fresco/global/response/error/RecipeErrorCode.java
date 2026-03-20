package com.example.fresco.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecipeErrorCode implements ErrorCode {
    NULL_RECIPE_RECOMMEND("RECIPE_ERR_001", HttpStatus.BAD_REQUEST, "레시피 생성에 실패했습니다."),
    NULL_RECIPE("RECIPE_ERR_002", HttpStatus.BAD_REQUEST, "해당 레시피 id가 없습니다."),
    RECIPE_NOT_FOUND("RECIPE_ERR_003", HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다."),
    RECIPE_FORBIDDEN("RECIPE_ERR_004", HttpStatus.FORBIDDEN, "이 레시피에 접근 권한이 없습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}