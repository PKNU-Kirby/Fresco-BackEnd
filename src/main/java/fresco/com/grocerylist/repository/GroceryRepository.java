package fresco.com.grocerylist.repository;

import fresco.com.grocerylist.domain.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryRepository extends JpaRepository<GroceryItem, Long>{
}
