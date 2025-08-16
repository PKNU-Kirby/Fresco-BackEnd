package com.example.fresco.user.test;

import com.example.fresco.auth.domain.Provider;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestUserDataCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        entityManager.createNativeQuery("DELETE FROM refreshtokens").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM users").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1").executeUpdate();

        User testUser = new User(Provider.NAVER, "PROVIDER_ID", "테스트123", "fcmToken");
        userRepository.save(testUser);
    }
}

