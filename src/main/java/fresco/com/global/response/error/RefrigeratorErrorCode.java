package fresco.com.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RefrigeratorErrorCode implements ErrorCode {
    NULL_REFRIGERATOR("REFRIGERATOR_ERR_001", HttpStatus.BAD_REQUEST, "냉장고가 존재하지 않습니다.");

    private final String developCode;
    private final HttpStatus httpStatus;
    private final String errorDescription;
}