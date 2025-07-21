package fresco.com.refrigerator.domain.repository;

import fresco.com.refrigerator.domain.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
}
