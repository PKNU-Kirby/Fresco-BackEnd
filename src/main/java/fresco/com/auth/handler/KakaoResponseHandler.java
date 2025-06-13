package fresco.com.auth.handler;

import fresco.com.auth.dto.response.KakaoResponse;
import fresco.com.auth.dto.response.OAuth2Response;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoResponseHandler implements OAuth2ResponseHandler {
    @Override
    public boolean supports(final String registrationId) {
        return "kakao".equalsIgnoreCase(registrationId);
    }

    @Override
    public OAuth2Response createResponse(final Map<String, Object> attributes) {
        return new KakaoResponse(attributes);
    }
}
