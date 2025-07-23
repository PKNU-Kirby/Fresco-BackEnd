package com.example.fresco.ingredient.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.IngredientErrorCode;
import com.example.fresco.ingredient.controller.dto.request.IngredientFilterRequest;
import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.ingredient.domain.Ingredient;
import com.example.fresco.ingredient.domain.repository.IngredientRepository;
import com.example.fresco.ingredient.service.util.UpdateIngredientConditionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final UpdateIngredientConditionManager updateIngredientConditionManager;

    @Transactional(readOnly = true)
    public List<IngredientResponse> getIngredients(Long refrigeratorId, IngredientFilterRequest filter) {
        Sort sortType = getSortType(filter);
        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), sortType);
        return ingredientRepository.findByRefrigeratorIdAndCategoryIdIn(refrigeratorId, filter.getCategoryIds(), pageRequest)
                .stream()
                .map(ingredient -> IngredientResponse.from(ingredient))
                .collect(Collectors.toList());
    }

    private Sort getSortType(IngredientFilterRequest filter) {
        if (filter.getSort().equals("expirationDateDesc"))
            return Sort.by(Sort.Direction.DESC, "expirationDate");
        else
            return Sort.by(Sort.Direction.ASC, "expirationDate");
    }

    @Transactional
    public IngredientResponse updateIngredient(UpdateIngredientConditionCommand updateIngredientConditionCommand) {
        Ingredient ingredient = ingredientRepository.findById(updateIngredientConditionCommand.id())
                .orElseThrow(() -> new RestApiException(IngredientErrorCode.NULL_INGREDIENT));

        updateIngredientConditionManager.updateContract(ingredient, updateIngredientConditionCommand);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return IngredientResponse.from(savedIngredient);
    }
}