package com.example.fresco.ingredient.service.util.ocr;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.properties.NaverOcrProperties;
import com.example.fresco.ingredient.controller.dto.response.ocr.ReceiptResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// 1. 외부 API 클라이언트 - infrastructure/external/naver
// NaverOcrClient.java
@Component
@RequiredArgsConstructor
public class NaverOcrClient {

    private final NaverOcrProperties naverOcrProperties;
    private final ObjectMapper objectMapper;

    public ReceiptResponse callOcrApi(String base64Image) {
        try {
            URL url = new URL(naverOcrProperties.getOcr_url());
            HttpURLConnection connection = createConnection(url);

            String requestBody = buildRequestBody(base64Image);
            sendRequest(connection, requestBody);

            String responseBody = getResponse(connection);
            return objectMapper.readValue(responseBody, ReceiptResponse.class);

        } catch (Exception e) {
            throw new RestApiException(null);
        }
    }

    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        con.setRequestProperty("X-OCR-SECRET", naverOcrProperties.getOcr_api_key());
        return con;
    }

    private String buildRequestBody(String base64Image) throws Exception {
        Map<String, Object> image = new LinkedHashMap<>();
        image.put("format", "jpg");
        image.put("name", "demo");
        image.put("data", base64Image);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("version", "V2");
        payload.put("requestId", UUID.randomUUID().toString());
        payload.put("timestamp", System.currentTimeMillis());
        payload.put("images", List.of(image));

        return objectMapper.writeValueAsString(payload);
    }

    private void sendRequest(HttpURLConnection connection, String requestBody) throws IOException {
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(requestBody.getBytes());
            wr.flush();
        }
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? connection.getInputStream() : connection.getErrorStream()))) {

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }
}