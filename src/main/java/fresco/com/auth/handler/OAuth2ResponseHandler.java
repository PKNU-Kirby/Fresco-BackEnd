package fresco.com.auth.handler;

import fresco.com.auth.dto.response.OAuth2Response;

import java.util.Map;

public interface OAuth2ResponseHandler {
    boolean supports(final String registrationId);
    OAuth2Response createResponse(final Map<String, Object> attributes);
}
