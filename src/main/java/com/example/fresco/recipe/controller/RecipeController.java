package com.example.fresco.recipe.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.RecipeSuccessCode;
import com.example.fresco.recipe.controller.dto.OpenAiResponse;
import com.example.fresco.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public SuccessResponse<OpenAiResponse> getRecipe(@RequestBody String prompt) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_RECOMMEND_SUCCESS,
                recipeService.generateRecipe(prompt));
    }

    @PostMapping("/save")
    public SuccessResponse<String> saveRecipe(@RequestBody SaveRecipeRequest request) {
        recipeService.saveRecipe(request);
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_SAVE_SUCCESS);
    }
}
