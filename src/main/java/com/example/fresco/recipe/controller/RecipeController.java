package com.example.fresco.recipe.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.RecipeSuccessCode;
import com.example.fresco.recipe.controller.dto.response.OpenAiResponse;
import com.example.fresco.recipe.controller.dto.response.RecipeDetailResponse;
import com.example.fresco.recipe.controller.dto.request.RecipeCreateRequest;
import com.example.fresco.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/ai")
    public SuccessResponse<OpenAiResponse> getRecipe(@RequestBody String prompt) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_RECOMMEND_SUCCESS,
                recipeService.generateRecipe(prompt));
    }

    @PostMapping("/create")
    public SuccessResponse<RecipeDetailResponse> createRecipe(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RecipeCreateRequest recipeRequest) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_ADD_SUCCESS,
                recipeService.createRecipe(recipeRequest, userId));
    }

}
