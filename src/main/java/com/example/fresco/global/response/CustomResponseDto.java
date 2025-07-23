package com.example.fresco.global.response;

public record CustomResponseDto<T>(String code, String message, T result) {
}