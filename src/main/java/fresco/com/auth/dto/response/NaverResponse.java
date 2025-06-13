package fresco.com.auth.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record NaverResponse(@NotNull Map<String, Object> attributes) implements OAuth2Response {

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}

