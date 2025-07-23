package com.example.fresco.ingredient.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.refrigerator.domain.Refrigerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends BaseEntity {
    String name;
    LocalDate expirationDate;
    Integer quantity;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "refrigeratorId")
    private Refrigerator refrigerator;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    public Ingredient(Refrigerator refrigerator, Category category, String name, LocalDate expirationDate, Integer quantity) {
        this.refrigerator = refrigerator;
        this.category = category;
        this.name = name;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}