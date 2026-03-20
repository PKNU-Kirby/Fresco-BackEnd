package com.example.fresco.recipe.domain.Repository;

import com.example.fresco.recipe.domain.FavoritesRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface FavoriteRepository extends JpaRepository<FavoritesRecipe, Long> {
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

    @Modifying
    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);

    @Query("""
                select f.recipe.id
                from FavoritesRecipe f
                where f.user.id = :userId
            """)
    Set<Long> findAllRecipeIdsByUserId(@Param("userId") Long userId);
}
