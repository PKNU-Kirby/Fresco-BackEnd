package fresco.com.ingredient.util;

import fresco.com.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import fresco.com.ingredient.domain.Ingredient;

public interface UpdateIngredientConditionHandler {
    void update(Ingredient ingredient, UpdateIngredientConditionCommand updateContractConditionCommand);

}
