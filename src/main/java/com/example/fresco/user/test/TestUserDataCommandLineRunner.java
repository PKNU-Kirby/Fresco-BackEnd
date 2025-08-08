package com.example.fresco.user.test;

import com.example.fresco.auth.domain.Provider;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class TestUserDataCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {
        Provider provider = Provider.NAVER;
        String providerId = "PROVIDER_ID";
        String name = "테스트123";
        String fcmToken = "fcmToken";

        try {
            userRepository.findByProviderAndProviderId(provider, providerId)
                    .orElseGet(() -> userRepository.save(
                            User.builder()
                                    .provider(provider)
                                    .providerId(providerId)
                                    .userName(name)
                                    .fcmToken(fcmToken)
                                    .build()
                    ));
        } catch (DataIntegrityViolationException e) {
            userRepository.findByProviderAndProviderId(provider, providerId).orElseThrow(() -> e);
        }
    }
}

