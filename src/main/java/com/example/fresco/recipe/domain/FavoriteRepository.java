package com.example.fresco.recipe.domain;

import com.example.fresco.user.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Set;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("""
        select f.recipe.id
        from Favorite f
        where f.user.id = :userId
          and f.recipe.id in :recipeIds
    """)
    Set<Long> findRecipeIdsByUserIdAndRecipeIds(@Param("userId") Long userId,
                                                @Param("recipeIds") Collection<Long> recipeIds);
}
