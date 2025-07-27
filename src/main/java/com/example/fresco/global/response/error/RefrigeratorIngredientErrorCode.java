package com.example.fresco.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RefrigeratorIngredientErrorCode implements ErrorCode {
    NULL_REFRIGERATOR_INGREDIENT("REFRIGERATOR_INGREDIENT_ERR_001", HttpStatus.BAD_REQUEST, "냉장고가 해당 삭재료가 존재하지 않습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}