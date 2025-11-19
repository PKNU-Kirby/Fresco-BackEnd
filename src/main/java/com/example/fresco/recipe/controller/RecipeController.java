package com.example.fresco.recipe.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.RecipeSuccessCode;
import com.example.fresco.recipe.controller.dto.request.CookingRequest;
import com.example.fresco.recipe.controller.dto.request.RecipeCreateRequest;
import com.example.fresco.recipe.controller.dto.response.*;
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
@RequestMapping("/api/v1/recipe")
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

    // ai 추천 레시피 조회
    @GetMapping("/ai")
    public SuccessResponse<OpenAiRecipeDto> getRecipe(@RequestParam String prompt) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_RECOMMEND_SUCCESS,
                aiRecipeService.generateRecipe(prompt));
    }

    //ai 추천 레시피 저장
    @PostMapping("/ai/save")
    public SuccessResponse<RecipeDetailResponse> saveAiRecipe(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid OpenAiRecipeDto dto
    ) {
        RecipeDetailResponse result = aiRecipeService.saveFromOpenAi(userId, dto);
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_ADD_SUCCESS, result);
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
    @GetMapping("/list")
    public SuccessResponse<List<RecipeListResponse>> getRecipeList(
            @AuthenticationPrincipal Long userId) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_GET_SUCCESS,
                recipeService.getRecipeList(userId));
    }

    //레시피 수정
    @PutMapping("/replace/{recipeId}")
    public SuccessResponse<RecipeDetailResponse> replaceRecipe(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RecipeCreateRequest request) {
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

    //냉장고 재고 조회
    @GetMapping("/cook/stocks/{refrigeratorId}")
    public SuccessResponse<List<StockResponse>> getRecipeIngredientStocks(
            @PathVariable Long refrigeratorId,
            @RequestParam List<String> recipeIngredientNames
    ) {
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_FAVORITE_LIST_SUCCESS,
                recipeService.getRefrigeratorIngredientStocks(refrigeratorId, recipeIngredientNames));
    }

    //조리하기 식재료 차감
    @PostMapping("/cook/use-ingredients")
    public SuccessResponse<CookingResponse> useIngredients(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid CookingRequest request
    ) {
        CookingResponse result = recipeService.cookingUsage(userId, request);
        return SuccessResponse.of(RecipeSuccessCode.COOKING_DECREASE_SUCCESS, result);
    }

    //레시피 검색
    @GetMapping("/search")
    public SuccessResponse<List<RecipeListResponse>> search(
            @RequestParam("word") String word,
            @AuthenticationPrincipal Long userId
    ) {
        List<RecipeListResponse> result = recipeService.search(word, userId);
        return SuccessResponse.of(RecipeSuccessCode.RECIPE_SEARCH_SUCCESS, result);
    }

}
