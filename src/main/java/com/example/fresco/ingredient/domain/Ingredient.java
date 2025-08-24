package com.example.fresco.ingredient.domain;

import com.example.fresco.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ingredients")
public class Ingredient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SMALLINT")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @Column(nullable = false, length = 150)
    private String name;

    private Integer defaultUseByPeriod;

    public Ingredient(Category category, String name, Integer defaultUseByPeriod) {
        this.category = category;
        this.name = name;
        this.defaultUseByPeriod = defaultUseByPeriod;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDefaultUseByPeriod(Integer defaultUseByPeriod) {
        this.defaultUseByPeriod = defaultUseByPeriod;
    }
}