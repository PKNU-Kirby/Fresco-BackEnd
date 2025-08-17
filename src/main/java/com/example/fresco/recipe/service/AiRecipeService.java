package com.example.fresco.recipe.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.RecipeErrorCode;
import com.example.fresco.recipe.controller.dto.response.OpenAiResponse;
import com.example.fresco.recipe.util.OpenAIClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiRecipeService {

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public OpenAiResponse generateRecipe(String prompt) {
        try {
            String argumentsJson = openAIClient.callFunction(prompt);
            return objectMapper.readValue(argumentsJson, OpenAiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            throw new RestApiException(RecipeErrorCode.NULL_RECIPE_RECOMMEND);
        }
    }


}
