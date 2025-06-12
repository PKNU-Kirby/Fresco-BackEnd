package fresco.com.recipe.service;

import fresco.com.recipe.dto.OpenAiRequest;
import fresco.com.recipe.dto.OpenAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Service
public class RecipeService {
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String chat(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, String> userMessage = Map.of("role", "user", "content", prompt);
        OpenAiRequest request = new OpenAiRequest("gpt-3.5-turbo", List.of(userMessage));

        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<OpenAiResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                OpenAiResponse.class
        );

        return response.getBody().choices.get(0).message.content;
    }
}
