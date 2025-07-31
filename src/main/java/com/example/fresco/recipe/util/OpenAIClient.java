package com.example.fresco.recipe.util;

import com.example.fresco.recipe.controller.dto.FunctionCallingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAIClient {

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String openAiUrl;

    public String callFunction(String prompt){
        Map<String, Object> functionSchema = Map.of(
                "name", "generate_recipe",
                "description", "사용자 프롬프트에 맞는 요리 레시피를 JSON으로 생성합니다.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "title", Map.of("type", "string"),
                                "ingredients", Map.of("type", "array", "items", Map.of("type", "string")),
                                "steps", Map.of(
                                        "type", "array",
                                        "description", "조리 단계를 문자열 배열로 반환하며 각 단계 앞에 '1. ', '2. ' 형식의 번호를 붙여주세요.",
                                        "items", Map.of("type", "string")),
                                "substitutions", Map.of("type", "array", "items", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "original", Map.of("type", "string"),
                                                "substitute", Map.of("type", "string")
                                        ),
                                        "required", List.of("original", "substitute")
                                ))
                        ),
                        "required", List.of("title", "ingredients", "steps", "substitutions")
                )
        );

        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "functions", List.of(functionSchema),
                "function_call", "auto"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<FunctionCallingResponse> response = restTemplate.exchange(
                openAiUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        FunctionCallingResponse answer = response.getBody();
        if (answer == null || answer.choices().isEmpty()) {
            throw new IllegalStateException("OpenAI 응답이 비어 있습니다.");
        }
        return answer.choices().get(0).message().function_call().arguments();
    }
}
