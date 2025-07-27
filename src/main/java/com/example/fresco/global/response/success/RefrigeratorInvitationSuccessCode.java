package com.example.fresco.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum  RefrigeratorInvitationSuccessCode implements SuccessCode {
    SAVE_INVITATION_SUCCESS("INVITE_OK_001", HttpStatus.OK, "냉장고 그룹 초대 성공");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
