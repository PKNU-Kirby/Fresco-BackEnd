package com.example.fresco.recipe.domain.Repository;

import com.example.fresco.recipe.domain.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findAllByRecipeId(Long recipeId);

    List<Short> findAllIngredientIdByIdIn(List<Long> recipeIngredientIds);

    void deleteAllByRecipeId(Long recipeId);

    @Modifying
    void deleteAllByRecipe_IdIn(Collection<Long> recipeIds);
}
