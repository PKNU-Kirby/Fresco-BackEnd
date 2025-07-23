package com.example.fresco.global.response.success;

import com.example.fresco.global.response.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GrocerySuccessCode implements SuccessCode {
    GROCERY_SUCCESS("GROCEREY_001", HttpStatus.OK, "장바구니 성공");
//    LOGOUT_SUCCESS("AUTH_003", HttpStatus.OK, "로그아웃 성공");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
