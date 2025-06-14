package fresco.com.global.response.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements SuccessCode {
    LOGIN_SUCCESS("AUTH_001", HttpStatus.OK, "로그인 성공");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
