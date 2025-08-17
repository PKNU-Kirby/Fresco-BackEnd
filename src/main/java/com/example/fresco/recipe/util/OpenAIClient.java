package com.example.fresco.recipe.util;

import com.example.fresco.recipe.controller.dto.response.FunctionCallingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

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
                "description", "사용자 프롬프트에 맞는 요리 레시피를 한글 JSON으로 생성합니다.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "title", Map.of("type", "string"),
                                "ingredients", Map.of(
                                        "type", "array",
                                        "items", Map.of(
                                                "type", "object",
                                                "properties", Map.of(
                                                        "ingredient", Map.of("type", "string"),
                                                        "quantity",   Map.of("type", "number")
                                                ),
                                                "required", List.of("ingredient", "quantity")
                                        )
                                ),
                                "steps", Map.of(
                                        "type", "array",
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
                "messages", List.of(
                        Map.of("role", "system",
                                "content", "아래 JSON 형식을 반드시 지켜서 반환하라." +
                                        "레시피에 사용된 모든 재료를 표시하고, steps의 각 요소 앞에는 \"1. \",\"2. \" 이런식으로 숫자를 붙여 나오게 하라.:\n"
                                        + "{ title: string, ingredients: [{ingredient, quantity}], "
                                        + "steps: [string], substitutions: [{original, substitute}] }"),
                        Map.of("role", "assistant", "content",
                                """
                                {
                                  "title":"예시 볶음밥",
                                  "ingredients":[{"ingredient":"당근","quantity":"2개"}],
                                 "steps":["1. 팬에 기름을 두른다 ..."],
                                  "substitutions":[{"original":"당근","substitute":"애호박"}]
                                }
                                """),
                        Map.of("role", "user", "content", prompt)
                ),
                "tools", List.of(Map.of(
                        "type", "function",
                        "function", functionSchema
                )),
                "tool_choice", "auto"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<FunctionCallingResponse> response;
        try {
            response = restTemplate.exchange(
                    openAiUrl,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<FunctionCallingResponse>() {}
            );
        } catch (HttpClientErrorException e) {
            System.err.println("Status: " + e.getStatusCode());
            System.err.println("Body  : " + e.getResponseBodyAsString());
            throw e;
        }

        FunctionCallingResponse answer = response.getBody();
        if (answer == null || answer.choices().isEmpty()) {
            throw new IllegalStateException("OpenAI 응답이 비어 있습니다.");
        }

        FunctionCallingResponse.Choice choice = answer.choices().get(0);
        List<FunctionCallingResponse.Choice.Message.ToolCall> toolCalls = choice.message().toolCalls();

        if (toolCalls == null || toolCalls.isEmpty()) {
            throw new IllegalStateException("tool_calls가 비어 있습니다.");
        }

        return toolCalls.get(0).function().arguments();
    }
}
