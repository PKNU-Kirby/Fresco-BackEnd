package com.example.fresco.ingredient.controller.dto.response.ocr;

import java.util.List;

public record ReceiptResponse(List<ImageResult> images) {

}