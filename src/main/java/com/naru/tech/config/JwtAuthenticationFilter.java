package com.naru.tech.config;

import com.naru.tech.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    private static final List<String> WHITELIST = List.of(
            "/api/v1/auth/",
            "/api/v1/user/",
            "/api/v1/post/",
            "/api/v1/category/",
            "/api/v1/image/",
            "/swagger",
            "/v3/api-docs"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        return WHITELIST.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = extractToken(request, "accessToken");
        String refreshToken = extractToken(request, "refreshToken");

        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            authenticate(accessToken);
        }

        else if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
            reissueToken(response, refreshToken);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        String username = jwtProvider.getUsernameFromToken(token);
        UserDetails userDetails = userService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void reissueToken(HttpServletResponse response, String refreshToken) {
        Long userId = jwtProvider.getUserIdFromToken(refreshToken);
        String username = jwtProvider.getUsernameFromToken(refreshToken);

        if (!jwtProvider.verifyRefreshTokenWithRedis(userId, refreshToken)) {
            return;
        }

        String newAccessToken = jwtProvider.createAccessToken(userId, username);
        String newRefreshToken = jwtProvider.createRefreshToken(userId, username);

        jwtProvider.registerRefreshToken(userId, newRefreshToken);
        jwtProvider.setTokenCookie(response, newAccessToken, newRefreshToken);

        authenticate(newAccessToken);
    }

    private String extractToken(HttpServletRequest request, String tokenName) {
        Cookie cookie = WebUtils.getCookie(request, tokenName);
        return cookie != null ? cookie.getValue() : null;
    }
}