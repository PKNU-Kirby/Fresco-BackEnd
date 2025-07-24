package com.example.fresco.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GrocerySuccessCode implements SuccessCode {
    GROCERY_ADD_SUCCESS("GROCEREY_001", HttpStatus.OK, "장바구니 항목 추가 성공"),
    GROCERY_LIST_SUCCESS("GROCEREY_002", HttpStatus.OK, "장바구니 조회 성공"),
    GROCERY_UPDATE_SUCCESS("GROCEREY_003", HttpStatus.OK, "장바구니 수정 성공"),
    GROCERY_DELETE_SUCCESS("GROCEREY_004", HttpStatus.OK, "장바구니 삭제 성공");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
