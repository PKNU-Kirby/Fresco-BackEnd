package com.example.fresco.ingredient.domain.repository;

import com.example.fresco.ingredient.controller.dto.response.AutoCompleteSearchResponse;
import com.example.fresco.ingredient.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Short> {
    Optional<Ingredient> findByName(String name);

    List<Ingredient> findAllByIdIn(List<Short> ingredientIds);

    // 1순위: 정확한 시작 매치 (가장 높은 우선순위)
    @Query("SELECT new com.example.fresco.ingredient.controller.dto.response.AutoCompleteSearchResponse(i.id, i.name, i.category.id, i.category.name) " +
            "FROM Ingredient i WHERE i.name LIKE :keyword% " +
            "ORDER BY i.name ASC")
    List<AutoCompleteSearchResponse> findExactStartMatch(@Param("keyword") String keyword);

    // 2순위: 포함 매치 (중간에 포함)
    @Query("SELECT new com.example.fresco.ingredient.controller.dto.response.AutoCompleteSearchResponse(i.id, i.name, i.category.id, i.category.name) " +
            "FROM Ingredient i WHERE i.name LIKE %:keyword% " +
            "AND i.name NOT LIKE :keyword% " +
            "ORDER BY i.name ASC")
    List<AutoCompleteSearchResponse> findContainsMatch(@Param("keyword") String keyword);

    // 3순위: ngram FULLTEXT 검색 (유사 매치)
    @Query(value =
            "SELECT i.id, i.name, c.id, c.name, " +
                    "MATCH(i.name) AGAINST(?1 IN NATURAL LANGUAGE MODE) as score " +
                    "FROM ingredients as i " +
                    "JOIN categories as c ON i.categoryId = c.id " +
                    "WHERE MATCH(i.name) AGAINST(?1 IN NATURAL LANGUAGE MODE) " +
                    "AND i.name NOT LIKE CONCAT('%', ?1, '%') " +
                    "ORDER BY score DESC",
            nativeQuery = true)
    List<Object[]> findSimilarMatch(String keyword);
}