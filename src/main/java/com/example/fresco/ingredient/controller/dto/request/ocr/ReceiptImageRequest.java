package com.example.fresco.ingredient.controller.dto.request.ocr;

import org.springframework.web.multipart.MultipartFile;

public record ReceiptImageRequest(
        MultipartFile image
) {
}