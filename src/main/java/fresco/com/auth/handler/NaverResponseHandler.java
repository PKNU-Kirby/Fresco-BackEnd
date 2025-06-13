package fresco.com.auth.handler;

import fresco.com.auth.dto.response.NaverResponse;
import fresco.com.auth.dto.response.OAuth2Response;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NaverResponseHandler implements OAuth2ResponseHandler {

    @Override
    public boolean supports(final String registrationId) {
        return "naver".equalsIgnoreCase(registrationId);
    }

    @Override
    public OAuth2Response createResponse(final Map<String, Object> attributes) {
        return new NaverResponse((Map<String, Object>) attributes.get("response"));
    }
}
