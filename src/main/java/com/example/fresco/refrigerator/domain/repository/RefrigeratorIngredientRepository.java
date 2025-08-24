package com.example.fresco.refrigerator.domain.repository;

import com.example.fresco.ingredient.controller.dto.response.RefrigeratorIngredientResponse;
import com.example.fresco.refrigerator.domain.RefrigeratorIngredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RefrigeratorIngredientRepository extends JpaRepository<RefrigeratorIngredient, Long> {
    @Query("select new com.example.fresco.ingredient.controller.dto.response.RefrigeratorIngredientResponse(ri.id, ri.ingredient.id, ri.category.id, ri.ingredient.name, ri.expirationDate, ri.quantity) " +
            "from RefrigeratorIngredient ri " +
            "join ri.ingredient i " +
            "where ri.refrigerator.id = :refrigeratorId and ri.category.id in(:categoryIds) "
    )
    Page<RefrigeratorIngredientResponse> findByRefrigeratorIdAndCategoryIdIn(Long refrigeratorId, List<Long> categoryIds, Pageable pageable);

    @Query("""
                select ri
                from RefrigeratorIngredient ri
                join fetch ri.refrigerator r
                join fetch ri.ingredient ing
                where r.id = :refrigeratorId
                  and ri.expirationDate = :targetDate
                order by ri.expirationDate asc, ri.id asc
            """)
    List<RefrigeratorIngredient> findByRefrigeratorAndExpirationDate(
            Long refrigeratorId, LocalDate targetDate);

    @Query("""
              select ri from RefrigeratorIngredient ri
              join ri.ingredient i
              where ri.refrigerator.id = :refrigeratorId
                and i.name in :ingredientNames
            """)
    List<RefrigeratorIngredient> findAllByRefrigeratorIdAndIngredientNames(
            Long refrigeratorId, List<String> ingredientNames);
}