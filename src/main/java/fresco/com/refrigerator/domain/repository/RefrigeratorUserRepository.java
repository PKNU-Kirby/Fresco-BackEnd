package fresco.com.refrigerator.domain.repository;

import fresco.com.refrigerator.controller.dto.response.RefrigeratorInfoResponse;
import fresco.com.refrigerator.domain.RefrigeratorUser;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefrigeratorUserRepository extends JpaRepository<RefrigeratorUser, Long> {
    @Modifying
    @Query("delete from RefrigeratorUser ru where ru.refrigerator.id = :refrigeratorId")
    void deleteByRefrigeratorId(Long refrigeratorId);

    
    @Query("select new fresco.com.refrigerator.controller.dto.response.RefrigeratorInfoResponse(r.id, r.name) " +
            "from RefrigeratorUser ru join ru.refrigerator r " +
            "where ru.user.id = :userId")
    List<RefrigeratorInfoResponse> findAllByUserId(Long userId);

    Optional<RefrigeratorUser> findByRefrigeratorIdAndUserId(Long refrigeratorId, Long userId);
}
