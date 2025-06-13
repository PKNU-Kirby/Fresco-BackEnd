package fresco.com.auth.util;

import fresco.com.auth.dto.request.UserDetailRequest;
import fresco.com.global.exception.RestApiException;
import fresco.com.global.properties.JwtProperties;
import fresco.com.global.response.error.AuthErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    // AccessToken 생성
    public String generateAccessToken(final UserDetailRequest userDetail) {
        Claims claims = getClaimsFrom(userDetail);
        return getTokenFrom(claims, jwtProperties.getAccessTokenValidTime() * 1000);
    }

    // AccessToken용 Claim 생성
    private Claims getClaimsFrom(final UserDetailRequest userDetail) {
        Claims claims = Jwts.claims();
        claims.put("userId", userDetail.userId());
        return claims;
    }

    // RefrshToken 생성
    public String generateRefreshToken(final UserDetailRequest user, final Long tokenId) {
        Claims claims = getClaimsFrom(user, tokenId);
        return getTokenFrom(claims, jwtProperties.getRefreshTokenValidTime() * 1000);
    }

    // RefreshToken용 Claim 생성
    private Claims getClaimsFrom(final UserDetailRequest user, final Long tokenId) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.userId());
        claims.put("tokenId", tokenId);
        return claims;
    }

    // claim 정보로 Token 얻기
    private String getTokenFrom(final Claims claims, final long validTime) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(
                        Keys.hmacShaKeyFor(jwtProperties.getBytesSecretKey()),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    // AccessToken 값만 남도록 접두사 삭제
    public String extractAccessToken(final HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    public boolean isExpiredToken(final String token) {
        try {
            Claims claims = getClaimsByToken(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    // 토큰으로부터 유저 ID 얻기
    public Long getUserIdFromToken(final String token) {
        try {
            Claims claims = getClaimsByToken(token);
            return claims.get("userId", Long.class);
        } catch (ExpiredJwtException e) {
            throw new RestApiException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
        }
    }
    // 토큰으로부터 토큰 ID 얻기
    public Long getTokenIdFromToken(final String token) {
        try {
            Claims claims = getClaimsByToken(token);
            return Long.parseLong(String.valueOf(claims.get("tokenId")));
        } catch (ExpiredJwtException e) {
            throw new RestApiException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new RestApiException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private Claims getClaimsByToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getBytesSecretKey()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}