package com.example.fresco.recipe.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipes")
@NoArgsConstructor
@Getter
public class Recipe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Lob
    private String steps;

    private String url;

    @Builder
    public Recipe(User user, String title, String steps, String url) {
        this.user = user;
        this.title = title;
        this.steps = steps;
        this.url = url;
    }
    public void updateUrl(String url) {
        this.url = url;
    }
    public void updateTitle(String title) {
        this.title = title;
    }
    public void updateSteps(String steps) {
        this.steps = steps;
    }
}