package com.example.fresco.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroceryListErrorCode implements ErrorCode {
    NULL_GROCERY_LIST("GROCERY_ERR_001", HttpStatus.BAD_REQUEST, "장바구니가 존재하지 않습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}
