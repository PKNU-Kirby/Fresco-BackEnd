package com.example.fresco.grocerylist.repository;

import com.example.fresco.grocerylist.domain.GroceryList;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Long>{
    @EntityGraph(attributePaths = "items")
    Optional<GroceryList> findWithItemsById(Long grocerylistid);
}
