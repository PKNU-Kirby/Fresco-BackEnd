package com.example.fresco.ingredient.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.refrigerator.domain.Refrigerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends BaseEntity {
    String name;
    Date expirationDate;
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

    public Ingredient(Refrigerator refrigerator, Category category, String name, Date expirationDate, Integer quantity) {
        this.refrigerator = refrigerator;
        this.category = category;
        this.name = name;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
    }
}