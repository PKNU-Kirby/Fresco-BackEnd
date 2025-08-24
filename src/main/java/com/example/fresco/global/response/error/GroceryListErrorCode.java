package com.example.fresco.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroceryListErrorCode implements ErrorCode {
    NULL_GROCERY_LIST("GROCERY_ERR_001", HttpStatus.BAD_REQUEST, "장바구니가 존재하지 않습니다."),
    ITEM_NOT_FOUND("GROCERY_002", HttpStatus.NOT_FOUND, "해당 항목을 찾을 수 없습니다."),
    ITEM_LIST_EMPTY("GROCERY_003", HttpStatus.BAD_REQUEST, "삭제할 항목 목록이 비어 있습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}
