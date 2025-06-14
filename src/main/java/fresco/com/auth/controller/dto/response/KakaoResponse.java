package fresco.com.auth.controller.dto.response;

import fresco.com.auth.domain.Provider;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record KakaoResponse(
        @NotNull(message = "속성값 Map이 null이면 안 됩니다.")
        Map<String, Object> attributes) implements SocialResponse {

    @Override
    public Provider getProvider() {
        return Provider.KAKAO;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("properties");
        return kakaoAccount != null ? kakaoAccount.get("nickname").toString() : null;
    }
}
