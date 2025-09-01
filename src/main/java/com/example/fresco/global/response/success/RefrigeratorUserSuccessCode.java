package com.example.fresco.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RefrigeratorUserSuccessCode implements SuccessCode {
    REFRIGERATOR_USER_GET_SUCCESS("REFRIGERATOR_USER_OK_001", HttpStatus.OK, "냉장고 사용자 조회 성공"),
    REFRIGERATOR_USER_ADD_SUCCESS("REFRIGERATOR_USER_OK_002", HttpStatus.OK, "냉장고 그룹 인원 추가 성공"),
    REFRIGERATOR_USER_DELETE_SUCCESS("REFRIGERATOR_USER_OK_003", HttpStatus.OK, "냉장고 그룹 인원 삭제 성공"),

    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}