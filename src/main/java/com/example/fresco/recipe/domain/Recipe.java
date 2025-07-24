package com.example.fresco.recipe.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.refrigerator.domain.Refrigerator;
import com.example.fresco.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipe")
@NoArgsConstructor
@Getter
public class Recipe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refrigeratorId", nullable = false)
    private Refrigerator refrigerator;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    public Recipe(User user, Refrigerator refrigerator, String title, String description) {
        this.user = user;
        this.refrigerator = refrigerator;
        this.title = title;
        this.description = description;
    }
}