package com.example.fresco.user.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.recipe.domain.Recipe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorites",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_user_recipe",
                columnNames = {"userId", "recipeId"}))
@NoArgsConstructor
@Getter
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId", nullable = false)
    private Recipe recipe;
}