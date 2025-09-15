package com.example.fresco.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum IngredientSuccessCode implements SuccessCode {
    INGREDIENT_LIST_SUCCESS("INGREDIENT_OK_001", HttpStatus.OK, "식재료 전체 조회 성공"),
    INGREDIENT_UPDATE_SUCCESS("INGREDIENT_OK_002", HttpStatus.OK, "식재료 정보 업데이트 성공"),
    INGREDIENT_SAVE_SUCCESS("INGREDIENT_OK_003", HttpStatus.OK, "식자재 저장 성공"),
    RECEIPT_EXTRACT_SUCCESS("INGREDIENT_OK_004", HttpStatus.OK, "영수증 정보 추출 성공"),
    PHOTO_EXTRACT_SUCCESS("INGREDIENT_OK_005", HttpStatus.OK, "식재료 사진 정보 추출 성공"),
    AUTO_COMPLETE_RESEARCH_SUCCESS("INGREDIENT_OK_006", HttpStatus.OK, "삭자재 자동 완성 검색 성공"),
    INGREDIENT_DELETE_SUCCESS("INGREDIENT_OK_007", HttpStatus.OK, "삭자재 삭제 성공"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}