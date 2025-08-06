package com.example.fresco.ingredient.service.util.ocr;

import com.example.fresco.global.exception.RestApiException;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@UtilityClass
public class ImageUtils {

    public static String encodeToBase64(MultipartFile file) {
        try {
            return Base64.getEncoder().encodeToString(file.getBytes());
        } catch (IOException e) {
            throw new RestApiException(null, "이미지 Base64 인코딩 실패");
        }
    }

    public static void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RestApiException(null, "이미지 파일이 비어있습니다");
        }

        String contentType = file.getContentType();
        if (!isValidImageType(contentType)) {
            throw new RestApiException(null, "지원하지 않는 이미지 형식입니다: " + contentType);

        }

        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
            throw new RestApiException(null, "이미지 파일 크기가 너무 큽니다\"");

        }
    }

    private static boolean isValidImageType(String contentType) {
        return contentType != null &&
                (contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png"));
    }
}
