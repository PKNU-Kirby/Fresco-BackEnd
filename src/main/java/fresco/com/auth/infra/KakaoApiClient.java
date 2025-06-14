package fresco.com.auth.infra;

import fresco.com.auth.controller.dto.response.KakaoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoApiClient {

    private final WebClient web = WebClient.builder().baseUrl("https://kapi.kakao.com").build();

    public KakaoResponse profile(String accessToken) {
        return web.get().uri("/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .map(KakaoResponse::new)
                .block();
    }
}