package fresco.com.ingredient.controller;

import fresco.com.global.response.SuccessResponse;
import fresco.com.global.response.success.IngredientSuccessCode;
import fresco.com.ingredient.controller.dto.request.IngredientFilterRequest;
import fresco.com.ingredient.controller.dto.request.UpdateIngredientInfoRequest;
import fresco.com.ingredient.controller.dto.response.IngredientResponse;
import fresco.com.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ap1/v1/ingredient")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping("/{refrigeratorId}")
    public SuccessResponse<List<IngredientResponse>> getAllIngredients(
            @PathVariable Long refrigeratorId,
            @ModelAttribute IngredientFilterRequest filterRequest
    ) {
        List<IngredientResponse> ingredients = ingredientService.getIngredients(refrigeratorId, filterRequest);
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_LIST_SUCCESS, ingredients);
    }

    @PutMapping("/{ingredientId}")
    public SuccessResponse<IngredientResponse> updateIngredient(
            @PathVariable Long ingredientId,
            @RequestBody UpdateIngredientInfoRequest updateIngredientInfoRequest
    ) {
        IngredientResponse updatedIngredient = ingredientService.updateIngredient(
                updateIngredientInfoRequest.toCommand(ingredientId));
        return SuccessResponse.of(IngredientSuccessCode.INGREDIENT_UPDATE_SUCCESS, updatedIngredient);
    }
}