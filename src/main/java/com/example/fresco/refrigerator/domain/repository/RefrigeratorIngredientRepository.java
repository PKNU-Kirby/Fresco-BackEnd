package com.example.fresco.refrigerator.domain.repository;

import com.example.fresco.ingredient.controller.dto.response.IngredientResponse;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefrigeratorIngredientRepository extends JpaRepository<RefrigeratorIngredient, Long> {
    @Query("select new com.example.fresco.ingredient.controller.dto.response.IngredientResponse(i.id, i.name, i.category.id, i.category.name, ri.expirationDate) " +
            "from RefrigeratorIngredient ri " +
            "join ri.ingredient i " +
            "where ri.refrigerator.id = :refrigeratorId and ri.category.id in(:categoryIds) "
    )
    Page<IngredientResponse> findByRefrigeratorIdAndCategoryIdIn(Long refrigeratorId, List<Long> categoryIds, Pageable pageable);
}