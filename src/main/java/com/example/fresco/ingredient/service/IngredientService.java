package com.example.fresco.ingredient.service;

import com.example.fresco.global.exception.RestApiException;
import com.example.fresco.global.response.error.IngredientErrorCode;
import com.example.fresco.ingredient.controller.dto.request.IngredientFilterRequest;
import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.ingredient.controller.dto.response.PageInfo;
import com.example.fresco.ingredient.controller.dto.response.PageResponse;
import com.example.fresco.ingredient.service.util.UpdateIngredientConditionManager;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;
    private final UpdateIngredientConditionManager updateIngredientConditionManager;

    @Transactional(readOnly = true)
    public PageResponse<IngredientResponse> getIngredients(Long refrigeratorId, IngredientFilterRequest filter) {
        Sort sortType = getSortType(filter);
        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), sortType);
        Page<IngredientResponse> page = refrigeratorIngredientRepository.findByRefrigeratorIdAndCategoryIdIn(refrigeratorId, filter.getCategoryIds(), pageRequest);
        return new PageResponse<>(page.getContent(), PageInfo.getPageInfo(page));
    }

    private Sort getSortType(IngredientFilterRequest filter) {
        if (filter.getSort().equals("expirationDateDesc"))
            return Sort.by(Sort.Direction.DESC, "expirationDate");
        else
            return Sort.by(Sort.Direction.ASC, "expirationDate");
    }

    @Transactional
    public IngredientResponse updateIngredient(UpdateIngredientConditionCommand updateIngredientConditionCommand) {
        RefrigeratorIngredient refrigeratorIngredient = refrigeratorIngredientRepository.findById(updateIngredientConditionCommand.refrigeratorIngredientId())
                .orElseThrow(() -> new RestApiException(IngredientErrorCode.NULL_INGREDIENT));

        updateIngredientConditionManager.updateContract(refrigeratorIngredient, updateIngredientConditionCommand);
        RefrigeratorIngredient savedIngredient = refrigeratorIngredientRepository.save(refrigeratorIngredient);
        return IngredientResponse.from(savedIngredient);
    }
}