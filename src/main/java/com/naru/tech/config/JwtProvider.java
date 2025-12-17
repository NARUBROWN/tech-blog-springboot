package com.naru.tech.config;

import com.naru.tech.common.exception.TokenInvalidException;
import com.naru.tech.data.redis.object.RefreshToken;
import com.naru.tech.data.redis.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    private SecretKey key;

    // 액세스 토큰 만료 시간 30분
    private final Long accessTokenExpiration = (long) 1000 * 60 * 30;
    // 리프레시 토큰 만료 시간 7일
    private final Long refreshTokenExpiration = (long) 1000 * 60 * 60 * 24 * 7;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public void registerRefreshToken(Long userId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .ttl(this.convertMilliSecondsToSeconds(this.refreshTokenExpiration))
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String token) {
        if (token == null) return;
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
        refreshTokenRepository.delete(refreshToken);
    }

    public boolean verifyRefreshTokenWithRedis(Long userId, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findById(userId).orElseThrow(TokenInvalidException::new);
        if (token != null) {
            if (!refreshToken.getToken().equals(token)) {
                refreshTokenRepository.deleteById(userId);
                return false;
            }
        }
        return true;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return  Long.parseLong(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("userId").toString());
    }

    public String createAccessToken(Long userId, String username) {
        Map<String, Object> extraClaims = Map.of("userId", userId);
        return buildToken(extraClaims, username, accessTokenExpiration);
    }

    public String createRefreshToken(Long userId, String username) {
        Map<String, Object> extraClaims = Map.of("userId", userId);
        return buildToken(extraClaims, username, refreshTokenExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, String username, Long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }


    public void setTokenCookie(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(this.convertMilliSecondsToSeconds(this.accessTokenExpiration))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(this.convertMilliSecondsToSeconds(this.refreshTokenExpiration))
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    public void clearTokenCookies(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();


        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    private Long convertMilliSecondsToSeconds(Long time) {
        return time / 1000;
    }

}
