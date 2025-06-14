package fresco.com.auth.infra;

import fresco.com.auth.controller.dto.response.NaverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NaverApiClient {

    private final WebClient web = WebClient.builder().baseUrl("https://openapi.naver.com").build();

    public NaverResponse profile(String accessToken) {
        return web.get().uri("/v1/nid/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .map(NaverResponse::new)
                .block();
    }
}
