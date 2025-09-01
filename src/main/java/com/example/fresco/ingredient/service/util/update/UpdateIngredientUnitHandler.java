package com.example.fresco.ingredient.service.util.update;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class UpdateIngredientUnitHandler implements UpdateIngredientConditionHandler {

    @Override
    public void update(RefrigeratorIngredient refrigeratorIngredient, UpdateIngredientConditionCommand updateContractConditionCommand) {
        final String unit = updateContractConditionCommand.unit();

        if (Objects.nonNull(unit)) {
            refrigeratorIngredient.updateUnit(unit);
        }
    }
}