package com.example.fresco.recipe.domain.Repository;

import com.example.fresco.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("""
    select distinct r
    from Recipe r
    left join RecipeIngredient ri on ri.recipe = r
    left join Ingredient i on ri.ingredient = i
    where r.title like concat('%', :w, '%')
       or r.steps like concat('%', :w, '%')
       or i.name  like concat('%', :w, '%')
    """)
    List<Recipe> searchAll(@Param("w") String w);

}
