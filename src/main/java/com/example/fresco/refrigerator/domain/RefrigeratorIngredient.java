package com.example.fresco.refrigerator.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.ingredient.controller.dto.request.CreateIngredientsRequest;
import com.example.fresco.ingredient.domain.Category;
import com.example.fresco.ingredient.domain.Ingredient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    public void updateExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public static List<RefrigeratorIngredient> from(Refrigerator refrigerator, List<CreateIngredientsRequest> requests) {
        return requests.stream()
                .map(request -> {
                    new RefrigeratorIngredient();
                })
    }
}