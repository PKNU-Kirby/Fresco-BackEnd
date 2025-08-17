package com.example.fresco.ingredient.domain.repository;

import com.example.fresco.ingredient.controller.dto.response.AutoCompleteSearchResponse;
import com.example.fresco.ingredient.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

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
            "SELECT i.id, i.name, i.category.id, i.category.name " +
                    "MATCH(name) AGAINST(?1 IN NATURAL LANGUAGE MODE) as score " +
                    "FROM ingredients as i" +
                    "join categories as c on i.category.id = c.id " +
                    "WHERE MATCH(name) AGAINST(?1 IN NATURAL LANGUAGE MODE) " +
                    "AND name NOT LIKE %?1% " + // 이미 LIKE로 찾은 것들 제외
                    "ORDER BY score DESC",
            nativeQuery = true)
    List<Object[]> findSimilarMatch(String keyword);
}