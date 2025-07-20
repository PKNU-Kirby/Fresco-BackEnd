package fresco.com.grocerylist.repository;

import fresco.com.grocerylist.domain.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroceryRepository extends JpaRepository<Grocery,Long>{
}
