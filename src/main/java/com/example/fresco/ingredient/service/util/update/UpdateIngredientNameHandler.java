package com.example.fresco.ingredient.service.util.update;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class UpdateIngredientNameHandler implements UpdateIngredientConditionHandler {
    @Override
    public void update(RefrigeratorIngredient refrigeratorIngredient, UpdateIngredientConditionCommand updateContractConditionCommand) {
        final String name = updateContractConditionCommand.name();
        if (Objects.nonNull(name)) {
            refrigeratorIngredient.updateName(name);
        }
    }
}
