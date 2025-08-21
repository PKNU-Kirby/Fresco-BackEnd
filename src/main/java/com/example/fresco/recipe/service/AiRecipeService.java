package com.example.fresco.recipe.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.RecipeErrorCode;
import com.example.fresco.recipe.controller.dto.response.NearExpiryResponse;
import com.example.fresco.recipe.controller.dto.response.OpenAiResponse;
import com.example.fresco.recipe.util.OpenAIClient;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorIngredientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiRecipeService {

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;

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

    @Transactional
    public NearExpiryResponse listNearExpiryNameBuckets(Long refrigeratorId) {
        LocalDate today = LocalDate.now();

        List<String> day1 = refrigeratorIngredientRepository
                .findByRefrigeratorAndExpirationDate(refrigeratorId, today.plusDays(1))
                .stream()
                .map(RefrigeratorIngredient::getName)
                .toList();

        List<String> day2 = refrigeratorIngredientRepository
                .findByRefrigeratorAndExpirationDate(refrigeratorId, today.plusDays(2))
                .stream()
                .map(RefrigeratorIngredient::getName)
                .toList();

        List<String> day3 = refrigeratorIngredientRepository
                .findByRefrigeratorAndExpirationDate(refrigeratorId, today.plusDays(3))
                .stream()
                .map(RefrigeratorIngredient::getName)
                .toList();

        return new NearExpiryResponse(refrigeratorId, day1, day2, day3);
    }


}
