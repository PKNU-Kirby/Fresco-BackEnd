package com.example.fresco.recipe.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.RecipeSuccessCode;
import com.example.fresco.recipe.controller.dto.response.NearExpiryResponse;
import com.example.fresco.recipe.controller.dto.response.OpenAiResponse;
import com.example.fresco.recipe.controller.dto.response.RecipeDetailResponse;
import com.example.fresco.recipe.controller.dto.request.RecipeCreateRequest;
import com.example.fresco.recipe.controller.dto.response.RecipeListResponse;
import com.example.fresco.recipe.service.AiRecipeService;
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

    private final AiRecipeService aiRecipeService;
    private final RecipeService recipeService;

    //소비기한 임박 식재료 조회
    @GetMapping("/expiry/{refrigeratorId}")
    public SuccessResponse<NearExpiryResponse> getExpiryIngredientList(
            @PathVariable Long refrigeratorId
    ) {
        var result = aiRecipeService.listNearExpiryNameBuckets(refrigeratorId);
        return SuccessResponse.of(RecipeSuccessCode.EXPIRY_INGREDIENT_LIST_SUCCESS, result);
    }

    // ai 추천 조회
    @GetMapping("/ai")
    public SuccessResponse<OpenAiResponse> getRecipe(@RequestParam String prompt) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_RECOMMEND_SUCCESS,
                aiRecipeService.generateRecipe(prompt));
    }

    //사용자 레시피 생성
    @PostMapping("/create")
    public SuccessResponse<RecipeDetailResponse> createRecipe(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RecipeCreateRequest recipeRequest) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_ADD_SUCCESS,
                recipeService.createRecipe(recipeRequest, userId));
    }

    //레시피 상세 조회
    @GetMapping("/detail/{recipeId}")
    public SuccessResponse<RecipeDetailResponse> getRecipeDetail(
            @PathVariable Long recipeId) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_GET_SUCCESS,
                recipeService.getRecipeDetail(recipeId));
    }

    //레시피 리스트 조회
    @GetMapping("/list/{userId}")
    public SuccessResponse<List<RecipeListResponse>> getRecipeList(
            @PathVariable Long userId) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_GET_SUCCESS,
                recipeService.getRecipeList(userId));
    }

    //레시피 수정
    @PutMapping("/replace/{recipeId}")
    public SuccessResponse<RecipeDetailResponse> replaceRecipe(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RecipeCreateRequest request){
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_UPDATE_SUCCESS,
                recipeService.replaceRecipe(recipeId, request, userId));
    }

    //레시피 삭제
    @DeleteMapping("/delete/{recipeId}")
    public SuccessResponse<List<RecipeListResponse>> deleteRecipes(
            @PathVariable List<Long> recipeId,
            @AuthenticationPrincipal Long userId) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_DELETE_SUCCESS,
        recipeService.deleteRecipes(recipeId, userId));
    }

    //레시피 즐겨찾기 설정
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

    //레시피 공유
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

    //레시피 공유 조회
    @GetMapping("/share/{refrigeratorId}")
    public SuccessResponse<List<RecipeListResponse>> listShared(
            @PathVariable Long refrigeratorId,
            @AuthenticationPrincipal Long userId
    ) {
        List<RecipeListResponse> list = recipeService.listSharedRecipes(refrigeratorId, userId);
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_SHARE_LIST_SUCCESS, list);
    }

    //레시피 즐겨찾기 조회
    @GetMapping("/favorites")
    public SuccessResponse<List<RecipeListResponse>> listMyFavorites(
            @AuthenticationPrincipal Long userId
    ) {
        List<RecipeListResponse> result = recipeService.listFavoriteRecipes(userId);
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_FAVORITE_LIST_SUCCESS, result);
    }

}
