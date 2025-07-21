package fresco.com.auth.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Provider {
    KAKAO, NAVER;

    @JsonCreator
    public static Provider from(String value) {
        return Provider.valueOf(value.toUpperCase());
    }
}