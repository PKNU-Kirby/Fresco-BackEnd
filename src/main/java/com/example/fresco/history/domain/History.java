package com.example.fresco.history.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import com.example.fresco.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "history")
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
    @JoinColumn(name = "refrigeratorIngredientId", nullable = false)
    private RefrigeratorIngredient refrigeratorIngredient;

    private Integer usedQuantity = 0;

    public History(User user, RefrigeratorIngredient refrigeratorIngredient, Integer usedQuantity) {
        this.user = user;
        this.refrigeratorIngredient = refrigeratorIngredient;
        this.usedQuantity = usedQuantity;
    }
}