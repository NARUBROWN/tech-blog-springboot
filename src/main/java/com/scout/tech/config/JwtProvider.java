package com.scout.tech.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

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






}
