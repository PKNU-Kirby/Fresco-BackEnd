package com.example.fresco.recipe.domain.Repository;

import com.example.fresco.recipe.domain.Recipe;
import com.example.fresco.recipe.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {

    boolean existsByRefrigeratorIdAndRecipeId(Long refrigeratorId, Long recipeId);

    @Modifying
    void deleteByRefrigeratorIdAndRecipeId(Long refrigeratorId, Long recipeId);

    @Query("""
        select s.recipe
          from Share s
         where s.refrigerator.id = :refrigeratorId
    """)
    List<Recipe> findRecipesByRefrigeratorId(@Param("refrigeratorId") Long refrigeratorId);
}
