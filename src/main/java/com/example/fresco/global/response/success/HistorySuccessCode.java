package com.example.fresco.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HistorySuccessCode implements SuccessCode {
    GET_HISTORY_SUCCESS("HISTORY_OK_001", HttpStatus.OK, "식재료 변경 이력 조회 성공");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}