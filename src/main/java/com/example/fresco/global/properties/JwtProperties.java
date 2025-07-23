package com.example.fresco.global.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Getter
@Component
public class JwtProperties {
    @Value("${JWT.SECRET_KEY}")
    private String secretKey;

    @Value("${JWT.ACCESS_TOKEN.VALID_TIME}")
    private Long accessTokenValidTime;

    @Value("${JWT.REFRESH_TOKEN.VALID_TIME}")
    private Long refreshTokenValidTime;


    public byte[] getBytesSecretKey() {
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }
}