package com.example.fresco.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationSuccessCode implements SuccessCode {
    NOTIFICATION_PUSH_SUCCESS("NOTIFICATION_OK_001", HttpStatus.OK, "푸쉬 알림 전송 성공"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}