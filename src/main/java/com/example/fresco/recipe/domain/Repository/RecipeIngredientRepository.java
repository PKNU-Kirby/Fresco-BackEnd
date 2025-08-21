package com.example.fresco.recipe.domain.Repository;

import com.example.fresco.recipe.domain.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findAllByRecipeId(Long id);

    void deleteAllByRecipeId(Long recipeId);

}
