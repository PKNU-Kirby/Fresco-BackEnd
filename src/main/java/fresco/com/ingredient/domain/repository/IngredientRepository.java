package fresco.com.ingredient.domain.repository;

import fresco.com.ingredient.domain.Ingredient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @EntityGraph(attributePaths = {"category"})
    List<Ingredient> findByRefrigeratorIdAndCategoryIdIn(Long refrigeratorId, List<Integer> categoryIds, Pageable pageable);
}