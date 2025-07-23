package com.example.fresco.global.response.error;

import com.example.fresco.global.response.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum IngredientErrorCode implements ErrorCode {
    NULL_INGREDIENT("INGREDIENT_ERR_001", HttpStatus.BAD_REQUEST, "식재료가 존재하지 않습니다."),
    ;

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}
