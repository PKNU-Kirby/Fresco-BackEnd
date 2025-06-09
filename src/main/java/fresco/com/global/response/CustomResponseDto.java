package fresco.com.global.response;

public record CustomResponseDto<T> (String code, String message, T result) {
}