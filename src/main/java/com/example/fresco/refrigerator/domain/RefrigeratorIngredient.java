package com.example.fresco.refrigerator.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.ingredient.domain.Category;
import com.example.fresco.ingredient.domain.Ingredient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "refrigeratorIngredient")
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

    @Column(nullable = false, length = 150)
    private String name;

    private LocalDate expirationDate;

    private Integer quantity = 0;

}