package fresco.com.global.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NaverOcrProperties {

    @Value("${naver.ocr.url}")
    private String ocr_url;

    @Value("${naver.ocr.api-key-id}")
    private String ocr_api_key_id;

    @Value("${naver.ocr.api-key}")
    private String ocr_api_key;
}
