package com.naru.tech.service;

import com.naru.tech.common.enums.ErrorCode;
import com.naru.tech.common.exception.LoginException;
import com.naru.tech.config.JwtProvider;
import com.naru.tech.data.domain.User;
import com.naru.tech.data.dto.web.request.LoginRequest;
import com.naru.tech.data.dto.web.response.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(HttpServletResponse response, LoginRequest loginRequest) {
        User user = (User) userService.loadUserByUsername(loginRequest.username());

        if (!passwordEncoder.matches(loginRequest.password(), user.getHashedPassword())) {
            throw new LoginException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getUsername());

        jwtProvider.registerRefreshToken(user.getId(), refreshToken);

        jwtProvider.setTokenCookie(response, accessToken, refreshToken);

        return LoginResponse.fromEntity(user);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshToken = getCookie(request, "refreshToken");

        if (refreshToken == null) {
            jwtProvider.deleteRefreshToken(refreshToken.getValue());
        }
        jwtProvider.clearTokenCookies(response);
    }

    private Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) return cookie;
        }
        return null;
    }




}
