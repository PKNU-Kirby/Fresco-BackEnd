package com.example.fresco.ingredient.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.paging.PageResponse;
import com.example.fresco.global.response.success.IngredientSuccessCode;
import com.example.fresco.ingredient.controller.dto.request.IngredientFilterRequest;
import com.example.fresco.ingredient.controller.dto.request.SaveIngredientsRequest;
import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientInfoRequest;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.ingredient.controller.dto.response.ReceiptMatchListResponse;
import com.example.fresco.ingredient.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PostMapping("/{refrigeratorId}")
    public SuccessResponse<List<IngredientResponse>> saveIngredient(
            @PathVariable Long refrigeratorId,
            @Valid @RequestBody SaveIngredientsRequest request) {

        List<IngredientResponse> ingredients = ingredientService.saveIngredient(refrigeratorId, request);
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_LIST_SUCCESS, ingredients);
    }

    // 영수증으로 등록
    @PostMapping("/scan-receipt")
    public SuccessResponse<ReceiptMatchListResponse> scanReceipt(
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