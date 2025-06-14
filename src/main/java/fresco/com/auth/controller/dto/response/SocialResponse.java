package fresco.com.auth.controller.dto.response;

import fresco.com.auth.domain.Provider;
import fresco.com.user.domain.User;

public interface SocialResponse {
    Provider getProvider();
    String getProviderId();
    String getName();

    default User toEntity(){
        return User.of(this.getProvider(), this.getProviderId(), this.getName());
    }
}
