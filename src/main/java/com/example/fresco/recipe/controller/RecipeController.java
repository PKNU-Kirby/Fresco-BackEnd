package com.example.fresco.recipe.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.RecipeSuccessCode;
import com.example.fresco.recipe.controller.dto.response.OpenAiResponse;
import com.example.fresco.recipe.controller.dto.response.RecipeDetailResponse;
import com.example.fresco.recipe.controller.dto.request.RecipeCreateRequest;
import com.example.fresco.recipe.controller.dto.response.RecipeListResponse;
import com.example.fresco.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/detail/{recipeId}")
    public SuccessResponse<RecipeDetailResponse> getRecipeDetail(
            @PathVariable Long recipeId) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_GET_SUCCESS,
                recipeService.getRecipeDetail(recipeId));
    }

    @GetMapping("/list/{userId}")
    public SuccessResponse<List<RecipeListResponse>> getRecipeList(
            @PathVariable Long userId) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_GET_SUCCESS,
                recipeService.getRecipeList(userId));
    }

    @PutMapping("/replace/{recipeId}")
    public SuccessResponse<RecipeDetailResponse> replaceRecipe(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RecipeCreateRequest request){
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_UPDATE_SUCCESS,
                recipeService.replaceRecipe(recipeId, request, userId));
    }

    @DeleteMapping("/delete/{recipeId}")
    public SuccessResponse<List<RecipeListResponse>> deleteRecipes(
            @PathVariable List<Long> recipeId,
            @AuthenticationPrincipal Long userId) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_DELETE_SUCCESS,
        recipeService.deleteRecipes(recipeId, userId));
    }

    @PostMapping("/favorite/toggle/{recipeId}")
    public SuccessResponse<Map<String, Object>> toggleFavorite(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long recipeId
    ) {
        boolean favorite = recipeService.favoriteToggle(userId, recipeId);
        return SuccessResponse.of(
                RecipeSuccessCode.RECIPE_FAVORITE_SUCCESS,
                Map.of("recipeId", recipeId, "favorite", favorite)
        );
    }

    @PostMapping("/share/toggle/{refrigeratorId}/{recipeId}")
    public SuccessResponse<Map<String, Object>> toggleShare(
            @PathVariable Long refrigeratorId,
            @PathVariable Long recipeId
    ) {
        boolean shared = recipeService.ShareToggle(refrigeratorId, recipeId);
        return SuccessResponse.of(
                RecipeSuccessCode.RECIPE_SHARE_SUCCESS,
                Map.of("refrigeratorId", refrigeratorId, "recipeId", recipeId, "shared", shared)
        );
    }

    @GetMapping("/share/{refrigeratorId}")
    public SuccessResponse<List<RecipeListResponse>> listShared(
            @PathVariable Long refrigeratorId,
            @AuthenticationPrincipal Long userId
    ) {
        List<RecipeListResponse> list = recipeService.listSharedRecipes(refrigeratorId, userId);
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_SHARE_LIST_SUCCESS, list);
    }

}
