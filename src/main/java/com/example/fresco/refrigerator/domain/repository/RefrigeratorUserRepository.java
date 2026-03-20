package com.example.fresco.refrigerator.domain.repository;

import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorEditableResponse;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorGroupMemberResponse;
import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorInfoResponse;
import com.example.fresco.refrigerator.domain.RefrigeratorUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefrigeratorUserRepository extends JpaRepository<RefrigeratorUser, Long> {
    @Modifying
    @Query("delete from RefrigeratorUser ru where ru.refrigerator.id = :refrigeratorId")
    void deleteByRefrigeratorId(Long refrigeratorId);

    @Query("select new com.example.fresco.refrigerator.controller.dto.response.RefrigeratorInfoResponse(r.id, r.name, g.id) " +
            "from RefrigeratorUser ru " +
            "inner join ru.refrigerator r " +
            "inner join GroceryList g on g.refrigerator.id = r.id " +
            "where ru.user.id = :userId " +
            "order by r.createdDate asc")
    List<RefrigeratorInfoResponse> findAllRefrigeratorsByUserId(Long userId);

    Optional<RefrigeratorUser> findByRefrigeratorIdAndUserId(Long refrigeratorId, Long userId);

    @Query("select new com.example.fresco.refrigerator.controller.dto.response.RefrigeratorGroupMemberResponse(u.id, u.name) " +
            "from RefrigeratorUser ru join ru.user u " +
            "where ru.refrigerator.id = :refrigeratorId " +
            "order by ru.createdDate asc")
    List<RefrigeratorGroupMemberResponse> findAllUsersByRefrigeratorId(Long refrigeratorId);

    @Query(
            "select distinct ru.user.id " +
                    "from RefrigeratorUser ru " +
                    "join RefrigeratorIngredient ri " +
                    "on ru.refrigerator = ri.refrigerator " +
                    "where ri.expirationDate between current_date and :daysLater"
    )
    List<Long> findExpiringIngredientsWithinDays(LocalDate daysLater);

    @Query("""
        select ru.refrigerator.id
        from RefrigeratorUser ru
        where ru.user.id = :userId
    """)
    List<Long> findRefrigeratorIdsByUserId(@Param("userId") Long userId);
}
