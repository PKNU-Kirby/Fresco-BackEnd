package com.example.fresco.auth.domain.repository;

import com.example.fresco.auth.domain.RefreshToken;
import com.example.fresco.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long id);

    void deleteAllByUser(User user);
}