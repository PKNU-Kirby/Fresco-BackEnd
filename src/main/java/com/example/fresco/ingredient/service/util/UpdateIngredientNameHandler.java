package com.example.fresco.ingredient.service.util;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.domain.Ingredient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class UpdateIngredientNameHandler implements UpdateIngredientConditionHandler {
    @Override
    public void update(Ingredient ingredient, UpdateIngredientConditionCommand updateContractConditionCommand) {
        final String name = updateContractConditionCommand.name();
        if (Objects.nonNull(name)) {
            ingredient.updateName(name);
        }
    }
}
