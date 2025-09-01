package com.example.fresco.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${spring.cloud.aws.region.static}")
    private String awsRegion;

    @Value("${spring.cloud.aws.parameter-store.firebase-parameter}")
    private String firebaseParameterName;

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @Bean
    @ConditionalOnMissingBean  // FirebaseMessaging 타입의 빈이 없을 때만 생성 (테스트에서 Mock 사용 시 충돌 방지)
    public FirebaseMessaging firebaseMessaging() throws IOException {

        if (FirebaseApp.getApps().isEmpty()) {
            GoogleCredentials credentials;

            if ("local".equals(activeProfile)) {
                // Local: resources 파일 사용
                InputStream serviceAccount = getClass().getClassLoader()
                        .getResourceAsStream("firebase/serviceAccountKey.json");

                if (serviceAccount == null) {
                    throw new FileNotFoundException("Firebase 설정 파일을 찾을 수 없음: firebase/serviceAccountKey.json");
                }

                credentials = GoogleCredentials.fromStream(serviceAccount);
                log.info("Firebase 로컬 파일로 초기화: firebase/serviceAccountKey.json");

            } else {
                // AWS: Parameter Store 사용
                String firebaseConfigJson = getFirebaseConfigFromParameterStore();

                InputStream configStream = new ByteArrayInputStream(
                        firebaseConfigJson.getBytes(StandardCharsets.UTF_8)
                );

                credentials = GoogleCredentials.fromStream(configStream);
                log.info("Firebase Parameter Store로 초기화: {}", firebaseParameterName);
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options);
            log.info("Firebase 초기화 완료 - Profile: {}", activeProfile);
        }

        return FirebaseMessaging.getInstance();
    }

    private String getFirebaseConfigFromParameterStore() {
        SsmClient ssmClient = SsmClient.builder()
                .region(Region.of(awsRegion))
                .build();

        GetParameterRequest request = GetParameterRequest.builder()
                .name(firebaseParameterName)
                .withDecryption(true)
                .build();

        GetParameterResponse response = ssmClient.getParameter(request);
        return response.parameter().value();
    }
}