package fresco.com.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum IngredientSuccessCode implements SuccessCode {
    INGREDIENT_LIST_SUCCESS("INGREDIENT_OK_001", HttpStatus.OK, "식재료 전체 조회 성공"),
    INGREDIENT_UPDATE_SUCCESS("INGREDIENT_OK_002", HttpStatus.OK, "식재료 정보 업데이트 성공"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}