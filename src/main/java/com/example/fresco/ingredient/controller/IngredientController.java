package com.example.fresco.ingredient.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.success.IngredientSuccessCode;
import com.example.fresco.ingredient.controller.dto.request.IngredientFilterRequest;
import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientInfoRequest;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.global.response.paging.PageResponse;
import com.example.fresco.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ap1/v1/ingredient")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping("/{refrigeratorId}")
    public SuccessResponse<PageResponse<IngredientResponse>> getAllIngredients(
            @PathVariable Long refrigeratorId,
            @ModelAttribute IngredientFilterRequest filterRequest
    ) {
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_LIST_SUCCESS,
                ingredientService.getIngredients(refrigeratorId, filterRequest));
    }

    @PutMapping("/{refrigeratorIngredientId}")
    public SuccessResponse<IngredientResponse> updateIngredient(
            @PathVariable Long refrigeratorIngredientId,
            @RequestBody UpdateIngredientInfoRequest updateIngredientInfoRequest,
            @AuthenticationPrincipal Long userId
    ) {
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_UPDATE_SUCCESS,
                ingredientService.updateIngredient(updateIngredientInfoRequest.toCommand(userId, refrigeratorIngredientId)));
    }
}