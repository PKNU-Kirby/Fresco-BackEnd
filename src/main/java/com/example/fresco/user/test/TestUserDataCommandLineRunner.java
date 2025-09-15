package com.example.fresco.user.test;

import com.example.fresco.auth.domain.Provider;
import com.example.fresco.user.domain.DeviceType;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.UserFcmToken;
import com.example.fresco.user.domain.repository.UserFcmTokenRepository;
import com.example.fresco.user.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("local")
public class TestUserDataCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFcmTokenRepository userFcmTokenRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        entityManager.createNativeQuery("DELETE FROM refreshtokens").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM refrigeratorinvitations").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM userfcmtokens").executeUpdate();
        //entityManager.createNativeQuery("DELETE FROM users").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1").executeUpdate();

//        User inviter = userRepository.save(new User(Provider.NAVER, "PROVIDER_ID", "테스트1", "testFcmToken"));
//        User invitee = userRepository.save(new User(Provider.KAKAO, "PROVIDER_ID2", "테스트2", "testFcmToken2"));
//        userFcmTokenRepository.save(new UserFcmToken(inviter, "fcmToken", DeviceType.ANDROID, true));
//        userFcmTokenRepository.save(new UserFcmToken(invitee, "fcmToken2", DeviceType.ANDROID, true));
    }
}

