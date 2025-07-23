package com.example.fresco.receipt.controller.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ReceiptImageRequest(
        MultipartFile image
) {
}