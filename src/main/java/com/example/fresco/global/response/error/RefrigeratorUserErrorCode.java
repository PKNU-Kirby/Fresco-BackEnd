package com.example.fresco.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RefrigeratorUserErrorCode implements ErrorCode {
    NULL_REFRIGERATOR_USER("REFRIGERATOR_USER_ERR_001", HttpStatus.BAD_REQUEST, "초대 정보가 존재하지 않습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}