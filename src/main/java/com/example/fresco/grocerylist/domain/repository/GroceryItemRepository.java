package com.example.fresco.grocerylist.domain.repository;

import com.example.fresco.grocerylist.domain.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroceryItemRepository extends JpaRepository<GroceryItem, Long> {
    List<GroceryItem> findAllByGroceryListId(Long groceryListId);

    @Modifying
    void deleteAllByGroceryList_Id(Long groceryListId);
}

