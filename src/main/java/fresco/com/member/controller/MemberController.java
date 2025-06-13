package fresco.com.member.controller;

import fresco.com.auth.domain.CustomUserDetails;
import fresco.com.member.controller.dto.request.FcmTokenRequest;
import fresco.com.member.domain.Member;
import fresco.com.member.domain.repository.MemberRepository;
import fresco.com.member.domain.token.FcmToken;
import fresco.com.member.domain.token.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/fcm-token")
    public ResponseEntity<Void> registerFcmToken(@RequestBody FcmTokenRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = memberRepository.findById(userDetails.getUserId()).orElseThrow();

        if (fcmTokenRepository.findByToken(request.token()).isEmpty()) {
            fcmTokenRepository.save(FcmToken.of(member, request.token()));
        }
        return ResponseEntity.ok().build();
    }
}
