package com.example.fresco.recipe.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.AuthErrorCode;
import com.example.fresco.global.response.error.IngredientErrorCode;
import com.example.fresco.global.response.error.RecipeErrorCode;
import com.example.fresco.ingredient.domain.Category;
import com.example.fresco.ingredient.domain.Ingredient;
import com.example.fresco.ingredient.domain.repository.CategoryRepository;
import com.example.fresco.ingredient.domain.repository.IngredientRepository;
import com.example.fresco.recipe.controller.dto.response.NearExpiryResponse;
import com.example.fresco.recipe.controller.dto.response.OpenAiRecipeDto;
import com.example.fresco.recipe.controller.dto.response.RecipeDetailResponse;
import com.example.fresco.recipe.domain.Recipe;
import com.example.fresco.recipe.domain.RecipeIngredient;
import com.example.fresco.recipe.domain.Repository.RecipeIngredientRepository;
import com.example.fresco.recipe.domain.Repository.RecipeRepository;
import com.example.fresco.recipe.util.OpenAIClient;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorIngredientRepository;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiRecipeService {

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public OpenAiRecipeDto generateRecipe(String prompt) {
        try {
            String argumentsJson = openAIClient.callFunction(prompt);
            return objectMapper.readValue(argumentsJson, OpenAiRecipeDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            throw new RestApiException(RecipeErrorCode.NULL_RECIPE_RECOMMEND);
        }
    }

    @Transactional(readOnly = true)
    public NearExpiryResponse listNearExpiryNameBuckets(Long refrigeratorId) {
        LocalDate today = LocalDate.now();

        List<String> day1 = refrigeratorIngredientRepository
                .findByRefrigeratorAndExpirationDate(refrigeratorId, today.plusDays(1))
                .stream()
                .map(ri -> ri.getIngredient().getName())
                .toList();

        List<String> day2 = refrigeratorIngredientRepository
                .findByRefrigeratorAndExpirationDate(refrigeratorId, today.plusDays(2))
                .stream()
                .map(ri -> ri.getIngredient().getName())
                .toList();

        List<String> day3 = refrigeratorIngredientRepository
                .findByRefrigeratorAndExpirationDate(refrigeratorId, today.plusDays(3))
                .stream()
                .map(ri -> ri.getIngredient().getName())
                .toList();

        return new NearExpiryResponse(refrigeratorId, day1, day2, day3);
    }


    @Transactional
    public RecipeDetailResponse saveFromOpenAi(Long userId, OpenAiRecipeDto dto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        String steps = (dto.steps() == null) ? null : String.join("\n", dto.steps());

        Recipe recipe = Recipe.builder()
                .title(dto.title())
                .steps(steps)
                .url(null)       // 필요 시 세팅
                .user(user)
                .build();
        Recipe saved = recipeRepository.save(recipe);

        // substitutions: original -> substitute 맵
        Map<String, String> subMap = Optional.ofNullable(dto.substitutions())
                .orElse(List.of())
                .stream()
                .filter(s -> s != null && s.original() != null && !s.original().isBlank())
                .collect(Collectors.toMap(
                        s -> s.original().trim(),
                        s -> (s.substitute() == null) ? null : s.substitute().trim(),
                        (a, b) -> b
                ));

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        Category etcCategory = categoryRepository.findById(11L).get();
        for (OpenAiRecipeDto.Item it : Optional.ofNullable(dto.ingredients()).orElse(List.of())) {
            String ingredientName = (it == null || it.ingredientName() == null) ? null : it.ingredientName().trim();
            if (ingredientName == null || ingredientName.isEmpty()) {
                throw new RestApiException(IngredientErrorCode.NULL_INGREDIENT);
            }



            Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                    .orElseGet(() -> ingredientRepository.save(
                            Ingredient.builder().ingredientName(ingredientName).category(etcCategory).build()
                    ));

            RecipeIngredient ri = RecipeIngredient.builder()
                    .recipe(saved)
                    .ingredient(ingredient)
                    .quantity(it.quantity())
                    .unit((it.unit() == null) ? null : it.unit().trim())
                    .instead(subMap.getOrDefault(ingredientName, null)) // 대체재 매핑
                    .build();

            recipeIngredients.add(ri);
        }

        recipeIngredientRepository.saveAll(recipeIngredients);
        List<RecipeIngredient> items = recipeIngredientRepository.findAllByRecipeId(saved.getId());
        return RecipeDetailResponse.from(saved, items);
    }

}
