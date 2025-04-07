package com.scout.tech.config;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

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






}
