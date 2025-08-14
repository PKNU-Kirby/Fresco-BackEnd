package com.example.fresco.ingredient.service.util.update;

import com.example.fresco.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;

public interface UpdateIngredientConditionHandler {
    void update(RefrigeratorIngredient refrigeratorIngredient, UpdateIngredientConditionCommand updateContractConditionCommand);

}
