package fresco.com.auth.service;

import fresco.com.auth.domain.CustomUserDetails;
import fresco.com.auth.dto.response.OAuth2Response;
import fresco.com.auth.handler.OAuth2ResponseHandler;
import fresco.com.global.exception.RestApiException;
import fresco.com.global.response.error.AuthErrorCode;
import fresco.com.member.domain.Member;
import fresco.com.member.domain.MemberRole;
import fresco.com.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final List<OAuth2ResponseHandler> oauth2ResponseHandlers;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        OAuth2Response oAuth2Response = createOAuth2Response(userRequest.getClientRegistration().getRegistrationId(), user);
        Member savedMember = findOrCreateMember(oAuth2Response);

        return CustomUserDetails.from(savedMember);
    }

    public CustomUserDetails loadUserByUserId(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_MEMBER));

        return CustomUserDetails.from(member);
    }

    private OAuth2Response createOAuth2Response(String registrationId, OAuth2User oAuth2User) {
        for (OAuth2ResponseHandler oauth2ResponseHandler : oauth2ResponseHandlers) {
            if(oauth2ResponseHandler.supports(registrationId)) {
                return oauth2ResponseHandler.createResponse(oAuth2User.getAttributes());
            }
        }
        return null;
    }

    private Member findOrCreateMember(OAuth2Response userInfo) {
        String email = userInfo.getEmail();
        String name = userInfo.getName();
        List<MemberRole> roles = List.of(MemberRole.USER);

        return memberRepository.findMemberByEmail(email)
                .orElseGet(() -> memberRepository.save(Member.of(email, name, roles)));
    }
}
