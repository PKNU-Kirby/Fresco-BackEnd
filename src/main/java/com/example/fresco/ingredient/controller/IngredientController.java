package com.example.fresco.ingredient.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.paging.PageResponse;
import com.example.fresco.global.response.success.IngredientSuccessCode;
import com.example.fresco.ingredient.controller.dto.request.IngredientFilterRequest;
import com.example.fresco.ingredient.controller.dto.request.CreateIngredientsRequest;
import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientInfoRequest;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.ingredient.controller.dto.response.ReceiptMatchResponse;
import com.example.fresco.ingredient.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    // 수정 필요
    @PostMapping("/{refrigeratorId}")
    public SuccessResponse<IngredientResponse> createIngredient(
            @PathVariable Long refrigeratorId,
            @Valid @RequestBody CreateIngredientsRequest request) {

        IngredientResponse ingredient = ingredientService.createIngredient(refrigeratorId, request);
        return SuccessResponse.of(null, ingredient);
    }

    // 영수증으로 등록
    @PostMapping("/scan-receipt")
    public SuccessResponse<ReceiptMatchResponse> scanReceipt(
            @RequestPart("receipt") MultipartFile receiptImage) {
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_LIST_SUCCESS,
                ingredientService.registerFromReceipt(receiptImage));
    }

    // 식재료 사진으로 등록
//    @PostMapping("/scan-photo")
//    public SuccessResponse<List<IngredientResponse>> scanPhoto(
//            @RequestParam("ingredientImage") MultipartFile ingredientImage) {
//
//        List<IngredientResponse> ingredients = ingredientService.registerFromPhoto(refrigeratorId, ingredientImage);
//        return SuccessResponse.of()
//    }
}