package com.example.fresco.grocerylist.domain;

import com.example.fresco.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "grocerylistitems")
public class GroceryItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groceryListId")
    private GroceryList groceryList;

    private Integer quantity;

    private String name;

    private Boolean purchased;

    public GroceryItem(String name, Boolean purchased) {
        this.name = name;
        this.purchased = purchased;
    }

    public void updateName(String newName) {
        this.name = newName;
    }

    public void updatePurchased(Boolean newPurchased) {
        this.purchased = newPurchased;
    }

    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
    }
}