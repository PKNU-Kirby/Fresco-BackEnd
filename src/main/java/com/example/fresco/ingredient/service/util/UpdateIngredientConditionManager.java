package com.example.fresco.ingredient.service.util;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateIngredientConditionManager {
    private final List<UpdateIngredientConditionHandler> updateIngredientConditionHandlers;

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateContract(RefrigeratorIngredient refrigeratorIngredient, UpdateIngredientConditionCommand updateIngredientConditionCommand) {
        updateIngredientConditionHandlers
                .forEach(updateIngredientHandler ->
                        updateIngredientHandler.update(refrigeratorIngredient, updateIngredientConditionCommand));
    }
}