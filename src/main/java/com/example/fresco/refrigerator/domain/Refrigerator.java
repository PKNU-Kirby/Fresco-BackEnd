package com.example.fresco.refrigerator.domain;

import com.example.fresco.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refrigerators")
public class Refrigerator extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long creator;

    public Refrigerator(String name, Long creator) { this.name = name;  this.creator = creator; }

    public void changeName(String name) {
        this.name = name;
    }
}