package fresco.com.ingredient.util;

import fresco.com.ingredient.controller.dto.request.UpdateIngredientConditionCommand;
import fresco.com.ingredient.domain.Ingredient;
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
    public void updateContract(Ingredient ingredient, UpdateIngredientConditionCommand updateIngredientConditionCommand) {
        updateIngredientConditionHandlers
                .forEach(updateIngredientHandler ->
                        updateIngredientHandler.update(ingredient, updateIngredientConditionCommand));
    }
}