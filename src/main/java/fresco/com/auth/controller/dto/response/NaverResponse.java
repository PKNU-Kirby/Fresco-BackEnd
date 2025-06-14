package fresco.com.auth.controller.dto.response;

import fresco.com.auth.domain.Provider;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record NaverResponse(
        @NotNull(message = "null이면 안 됩니다.")
        Map<String, Object> attributes) implements SocialResponse {

    @Override
    public Provider getProvider() {
        return Provider.NAVER;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
