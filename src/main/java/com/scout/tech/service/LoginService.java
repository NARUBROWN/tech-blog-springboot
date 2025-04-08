package com.scout.tech.service;

import com.scout.tech.common.enums.ErrorCode;
import com.scout.tech.common.exception.LoginException;
import com.scout.tech.config.JwtProvider;
import com.scout.tech.data.domain.User;
import com.scout.tech.data.dto.request.LoginRequest;
import com.scout.tech.data.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginRequest loginRequest) {
        User user = (User) userService.loadUserByUsername(loginRequest.username());

        if (passwordEncoder.matches(loginRequest.password(), user.getHashedPassword())) {
            throw new LoginException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getUsername());

        return new TokenResponse(accessToken, refreshToken);
    }
}
