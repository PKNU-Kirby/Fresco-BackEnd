package com.example.fresco.ingredient.service.util;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class UpdateIngredientQuantityHandler implements UpdateIngredientConditionHandler {
    @Override
    public void update(RefrigeratorIngredient refrigeratorIngredient, UpdateIngredientConditionCommand updateContractConditionCommand) {
        final Integer quantity = updateContractConditionCommand.quantity();
        if (Objects.nonNull(quantity)) {
            refrigeratorIngredient.updateQuantity(quantity);
        }
    }
}