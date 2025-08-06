package com.example.fresco.user.test;

import com.example.fresco.auth.domain.Provider;
import com.example.fresco.user.domain.User;
import com.example.fresco.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestUserDataCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User testUser = new User(Provider.NAVER, "PROVIDER_ID", "테스트123", "fcmToken");
        userRepository.save(testUser);
    }
}

