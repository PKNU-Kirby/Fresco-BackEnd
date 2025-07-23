package com.example.fresco.auth.controller.dto.response;

import com.example.fresco.auth.domain.Provider;
import com.example.fresco.user.domain.User;

public interface SocialResponse {
    Provider getProvider();

    String getProviderId();

    String getName();

    default User toEntity() {
        return User.of(this.getProvider(), this.getProviderId(), this.getName());
    }
}
