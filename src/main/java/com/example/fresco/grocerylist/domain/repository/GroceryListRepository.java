package com.example.fresco.grocerylist.domain.repository;

import com.example.fresco.grocerylist.domain.GroceryList;
import com.example.fresco.refrigerator.domain.Refrigerator;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Long> {

    GroceryList findByRefrigerator(Refrigerator refrigerator);

    void deleteByRefrigerator_Id(@NotNull Long aLong);

    GroceryList findByRefrigerator_Id(@NotNull Long aLong);
}
