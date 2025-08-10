package com.example.fresco.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecipeSuccessCode implements SuccessCode {
    RECIPE_ADD_SUCCESS("RECIPE_001", HttpStatus.OK, "레시피 추가 성공"),
    RECIPE_GET_SUCCESS("RECIPE_002", HttpStatus.OK, "레시피 조회 성공"),
    RECIPE_UPDATE_SUCCESS("RECIPE_003", HttpStatus.OK, "레시피 수정 성공"),
    RECIPE_DELETE_SUCCESS("RECIPE_004", HttpStatus.OK, "레시피 삭제 성공"),
    RECIPE_RECOMMEND_SUCCESS("RECIPE_005", HttpStatus.OK, "레시피 추천 성공"),
    RECIPE_SAVE_SUCCESS("RECIPE_006", HttpStatus.OK, "레시피 저장 성공");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}

