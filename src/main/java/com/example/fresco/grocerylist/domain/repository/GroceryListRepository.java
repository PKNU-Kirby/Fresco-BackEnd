package com.example.fresco.grocerylist.domain.repository;

import com.example.fresco.grocerylist.domain.GroceryList;
import com.example.fresco.refrigerator.domain.Refrigerator;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Long>{

    GroceryList findByRefrigerator(Refrigerator refrigerator);
}
