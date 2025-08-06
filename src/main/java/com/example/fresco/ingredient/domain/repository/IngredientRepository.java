package com.example.fresco.ingredient.domain.repository;

import com.example.fresco.ingredient.domain.Ingredient;
import com.example.fresco.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);
}