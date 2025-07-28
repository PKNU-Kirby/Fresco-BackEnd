package com.example.fresco.refrigerator.domain.repository;

import com.example.fresco.refrigerator.controller.dto.response.RefrigeratorInvitationResponse;
import com.example.fresco.refrigerator.domain.RefrigeratorInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefrigeratorInvitationRepository extends JpaRepository<RefrigeratorInvitation, Long> {
    @Query("select new com.example.fresco.refrigerator.controller.dto.response.RefrigeratorInvitationResponse(rv.id, r.id, r.name, u.id, u.name) " +
            "from RefrigeratorInvitation rv join rv.refrigerator r join rv.inviter u " +
            "where rv.id = :refrigeratorInvitationId")
    Optional<RefrigeratorInvitationResponse> findInvitationInfoById(Long refrigeratorInvitationId);
}