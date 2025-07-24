package com.example.fresco.refrigerator.domain;

import com.example.fresco.global.domain.BaseEntity;
import com.example.fresco.grocerylist.domain.GroceryList;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refrigerator extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Refrigerator(String name) {
        this.name = name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    @OneToOne(mappedBy = "refrigerator", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private GroceryList groceryList;

    public void setGroceryList(GroceryList groceryList) {
        this.groceryList = groceryList;
        if (groceryList.getRefrigerator() != this) {
            groceryList.setRefrigerator(this);
        }
    }
}