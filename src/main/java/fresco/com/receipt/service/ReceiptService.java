package fresco.com.receipt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fresco.com.global.properties.NaverOcrProperties;
import fresco.com.receipt.controller.dto.request.Base64EncodingImage;
import fresco.com.receipt.controller.dto.request.ReceiptImageRequest;
import fresco.com.receipt.controller.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    public final NaverOcrProperties naverOcrProperties;
    private final ObjectMapper objectMapper;

    public List<FoodPair> postReceiptImage(ReceiptImageRequest receiptImage) {
        String base64Image = null;
        try {
            base64Image = Base64.getEncoder().encodeToString(receiptImage.image().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return callNaverOcr(new Base64EncodingImage(base64Image));
    }

    private List<FoodPair> callNaverOcr(Base64EncodingImage receiptImage) {
        ReceiptResponse receiptResponse = null;
        try {
            URL url = new URL(naverOcrProperties.getOcr_url());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", naverOcrProperties.getOcr_api_key());

            // JSON 바디 구성 (Jackson용 Map)
            Map<String, Object> image = new LinkedHashMap<>();
            image.put("format", "jpg");
            image.put("name", "demo");
            image.put("data", receiptImage.base64Image());

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("version", "V2");
            payload.put("requestId", UUID.randomUUID().toString());
            payload.put("timestamp", System.currentTimeMillis());
            payload.put("images", List.of(image));

            // JSON 문자열 변환
            String postParams = objectMapper.writeValueAsString(payload);

            // 요청 전송
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(postParams.getBytes());
            wr.flush();
            wr.close();

            // 응답 수신
            int responseCode = con.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode == 200 ? con.getInputStream() : con.getErrorStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            // JSON → 객체 변환
            receiptResponse = objectMapper.readValue(response.toString(), ReceiptResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<FoodPair> foods = receiptResponse.images().stream()
                .flatMap(image -> image.receipt().result().subResults().stream())
                .flatMap(sub -> sub.items().stream())
                .map(item -> new FoodPair(item.name().text(), item.count().text()))
                .collect(Collectors.toList());

        return foods;
    }
}