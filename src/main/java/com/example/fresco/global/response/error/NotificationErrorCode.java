package com.example.fresco.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements ErrorCode {
    NOTIFICATION_SEND_FAILED("NOTIFICATION_ERR_001", HttpStatus.BAD_REQUEST, "식재료 알림 전송에 실패하였습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}
