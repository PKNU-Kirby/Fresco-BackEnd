package com.example.fresco.history.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import com.example.fresco.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "histories")
@NoArgsConstructor
@Getter
public class History extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refrigeratorIngredientId", nullable = true)
    private RefrigeratorIngredient refrigeratorIngredient;

    private String ingredientName;
    private String unit;
    private Double usedQuantity = 0.0;

    public History(User user, RefrigeratorIngredient refrigeratorIngredient, String ingredientName, String unit, Double usedQuantity) {
        this.user = user;
        this.ingredientName = ingredientName;
        this.unit = unit;
        this.refrigeratorIngredient = refrigeratorIngredient;
        this.usedQuantity = usedQuantity;
    }
}