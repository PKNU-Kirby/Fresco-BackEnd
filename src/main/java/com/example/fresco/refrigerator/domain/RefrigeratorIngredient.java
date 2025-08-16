package com.example.fresco.refrigerator.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.ingredient.domain.Category;
import com.example.fresco.ingredient.domain.Ingredient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "refrigeratoringredients")
@NoArgsConstructor
@Getter
public class RefrigeratorIngredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refrigeratorId", nullable = false)
    private Refrigerator refrigerator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredientId", nullable = false)
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    private LocalDate expirationDate;

    public RefrigeratorIngredient(Refrigerator refrigerator, Ingredient ingredient, Category category, LocalDate expirationDate) {
        this.refrigerator = refrigerator;
        this.ingredient = ingredient;
        this.category = category;
        this.expirationDate = expirationDate;
    }

    public void updateExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}