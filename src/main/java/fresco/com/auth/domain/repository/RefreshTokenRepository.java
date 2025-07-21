package fresco.com.auth.domain.repository;

import fresco.com.auth.domain.RefreshToken;
import fresco.com.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long id);

    void deleteAllByUser(User user);
}