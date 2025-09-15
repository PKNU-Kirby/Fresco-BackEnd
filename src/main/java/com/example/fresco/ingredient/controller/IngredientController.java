package com.example.fresco.ingredient.controller;

import com.example.fresco.global.response.SuccessResponse;
import com.example.fresco.global.response.paging.PageResponse;
import com.example.fresco.global.response.success.IngredientSuccessCode;
import com.example.fresco.ingredient.controller.dto.request.IngredientFilterRequest;
import com.example.fresco.ingredient.controller.dto.request.SaveIngredientsRequest;
import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientInfoRequest;
import com.example.fresco.ingredient.controller.dto.response.AutoCompleteSearchResponse;
import com.example.fresco.ingredient.controller.dto.response.RefrigeratorIngredientResponse;
import com.example.fresco.ingredient.controller.dto.response.ReceiptOcrMappingResponse;
import com.example.fresco.ingredient.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("ap1/v1/ingredient")
@RequiredArgsConstructor
@Slf4j
public class IngredientController {
    private final IngredientService ingredientService;

    // 식자재 조회
    @GetMapping("/{refrigeratorId}")
    public SuccessResponse<PageResponse<RefrigeratorIngredientResponse>> getAllIngredients(
            @PathVariable Long refrigeratorId,
            @ModelAttribute IngredientFilterRequest filterRequest
    ) {
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_LIST_SUCCESS,
                ingredientService.getIngredients(refrigeratorId, filterRequest));
    }

    // 식자재 업데이트
    @PutMapping("/{refrigeratorIngredientId}")
    public SuccessResponse<RefrigeratorIngredientResponse> updateIngredient(
            @PathVariable Long refrigeratorIngredientId,
            @RequestBody UpdateIngredientInfoRequest updateIngredientInfoRequest,
            @AuthenticationPrincipal Long userId
    ) {
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_UPDATE_SUCCESS,
                ingredientService.updateIngredient(updateIngredientInfoRequest.toCommand(userId, refrigeratorIngredientId)));
    }

    // 식자재 저장
    @PostMapping("/{refrigeratorId}")
    public SuccessResponse<List<RefrigeratorIngredientResponse>> saveIngredient(
            @PathVariable Long refrigeratorId,
            @Valid @RequestBody SaveIngredientsRequest request) {

        List<RefrigeratorIngredientResponse> ingredients = ingredientService.saveIngredient(refrigeratorId, request);
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_SAVE_SUCCESS, ingredients);
    }

    // 영수증으로 등록
    @PostMapping(value = "/scan-receipt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<List<ReceiptOcrMappingResponse>> scanReceipt(
            @RequestPart("receipt") MultipartFile receiptImage) {
        long startTime = System.currentTimeMillis();
        log.info("시작 시간 : {}", startTime);
        List<ReceiptOcrMappingResponse> result = ingredientService.registerFromReceipt(receiptImage);
        long endTime = System.currentTimeMillis();
        log.info("종료 시간 : {}", endTime);
        return SuccessResponse.of(IngredientSuccessCode.RECEIPT_EXTRACT_SUCCESS, result);
    }

    // 식재료 사진으로 등록
    @PostMapping(value = "/scan-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<List<RefrigeratorIngredientResponse>> scanPhoto(@RequestParam("ingredientImage") MultipartFile ingredientImage) {
        return SuccessResponse.of(IngredientSuccessCode.PHOTO_EXTRACT_SUCCESS,
                ingredientService.registerFromPhoto(ingredientImage));
    }

    // 삭자재 자동 완성 검색
    @GetMapping("/auto-complete")
    public SuccessResponse<List<AutoCompleteSearchResponse>> getAllIngredients(
            @RequestParam String keyword
    ) {
        return SuccessResponse.of(IngredientSuccessCode.AUTO_COMPLETE_RESEARCH_SUCCESS,
                ingredientService.searchAutoComplete(keyword));
    }

    // 식자재 삭제
    @DeleteMapping
    public SuccessResponse<String> deleteIngredients(
            @RequestParam List<Long> ids
    ) {
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_DELETE_SUCCESS,
                ingredientService.deleteIngredients(ids));
    }
}