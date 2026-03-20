package com.example.fresco.recipe.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.ingredient.domain.Ingredient;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipeIngredients")
@NoArgsConstructor
@Getter
public class RecipeIngredient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredientId", nullable = false)
    private Ingredient ingredient;

    private Double quantity;
    private String unit;
    private String instead;

    @Builder
    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Double quantity, String unit, String instead) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
        this.instead = instead;
    }
}
