package com.example.fresco.ingredient.service.util;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.ingredient.domain.Ingredient;

public interface UpdateIngredientConditionHandler {
    void update(Ingredient ingredient, UpdateIngredientConditionCommand updateContractConditionCommand);

}
