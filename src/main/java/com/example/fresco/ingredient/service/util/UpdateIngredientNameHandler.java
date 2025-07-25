package com.example.fresco.ingredient.service.util;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.domain.Ingredient;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import com.example.fresco.refrigerator.domain.repository.RefrigeratorIngredientRepository;
import org.springframework.stereotype.Service;

import java.sql.Ref;
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
