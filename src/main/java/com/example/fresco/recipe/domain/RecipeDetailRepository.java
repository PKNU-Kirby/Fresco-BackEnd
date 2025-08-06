package com.example.fresco.recipe.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeDetailRepository extends JpaRepository<Recipe, Long> {
}
