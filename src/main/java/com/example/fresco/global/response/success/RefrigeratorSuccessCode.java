package com.example.fresco.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RefrigeratorSuccessCode implements SuccessCode {
    REFRIGERATOR_CREATE_SUCCESS("REFRIGERATOR_OK_001", HttpStatus.CREATED, "냉장고 생성 성공"),
    REFRIGERATOR_DELETE_SUCCESS("REFRIGERATOR_OK_002", HttpStatus.OK, "냉장고 삭제 성공"),
    REFRIGERATOR_UPDATE_SUCCESS("REFRIGERATOR_OK_003", HttpStatus.OK, "냉장고 정보 변경 성공"),
    REFRIGERATOR_LIST_SUCCESS("REFRIGERATOR_OK_004", HttpStatus.OK, "냉장고 전체 조회 성공"),
    REFRIGERATOR_PERMISSION_SUCCESS("REFRIGERATOR_OK_005", HttpStatus.OK, "냉장고 편집 권한 조회 성공"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}