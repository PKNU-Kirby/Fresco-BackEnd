package fresco.com.user.domain.repository;

import fresco.com.auth.domain.Provider;
import fresco.com.auth.domain.UserInfo;
import fresco.com.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);

    @Query("select new fresco.com.auth.domain.UserInfo(u.id, u.provider, u.providerId, u.name) from User u where u.id = :userId")
    UserInfo findUserInfoById(Long userId);
}