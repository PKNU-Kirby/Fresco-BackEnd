package com.example.fresco.ingredient.service.util;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.domain.Ingredient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class UpdateIngredientQuantityHandler implements UpdateIngredientConditionHandler {
    @Override
    public void update(Ingredient ingredient, UpdateIngredientConditionCommand updateContractConditionCommand) {
        final Integer quantity = updateContractConditionCommand.quantity();
        if (Objects.nonNull(quantity)) {
            ingredient.updateQuantity(quantity);
        }
    }
}