package com.example.fresco.global.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NaverOcrProperties {

    @Value("${NAVER.OCR.URL}")
    private String ocr_url;

    @Value("${NAVER.OCR.API-KEY}")
    private String ocr_api_key;
}
