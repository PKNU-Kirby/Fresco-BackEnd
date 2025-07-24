package com.example.fresco.grocerylist.repository;

import com.example.fresco.grocerylist.domain.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryRepository extends JpaRepository<GroceryItem, Long> {
}

