package com.example.fresco.grocerylist.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.refrigerator.domain.Refrigerator;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "groceryLists")
public class GroceryList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refrigeratorId")
    private Refrigerator refrigerator;

    private Integer totalAmount;

    @Builder
    public GroceryList(Integer totalAmount, Refrigerator refrigerator) {
        this.totalAmount = totalAmount;
        this.refrigerator = refrigerator;
    }
}