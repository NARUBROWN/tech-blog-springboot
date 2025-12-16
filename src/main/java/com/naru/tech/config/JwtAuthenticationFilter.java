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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println(
                "[JWT FILTER] " +
                        request.getMethod() + " " +
                        request.getRequestURI()
        );

        filterChain.doFilter(request, response);
        // accessToken 추출
        String accessToken = this.extractToken(request, "accessToken");
        // refreshToken 추출
        String refreshToken = this.extractToken(request, "refreshToken");
        // accessToken 유효 확인
        if (jwtProvider.validateToken(accessToken)) {
            String username = jwtProvider.getUsernameFromToken(accessToken);
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // SecurityContext에 Authentication 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // accessToken 유효하지 않으면
            // refreshToken 유효한지 확인
            if (jwtProvider.validateToken(refreshToken)) {
                // accessToken 재발급 위해 Username, UserId를 refreshToken에서 추출
                String username = jwtProvider.getUsernameFromToken(refreshToken);
                Long userId = jwtProvider.getUserIdFromToken(refreshToken);
                if (jwtProvider.verifyRefreshTokenWithRedis(userId, refreshToken)) {
                    String regenerateAccessToken = jwtProvider.createAccessToken(userId, username);
                    String regenerateRefreshToken = jwtProvider.createRefreshToken(userId, username);
                    jwtProvider.registerRefreshToken(userId, regenerateRefreshToken);
                    jwtProvider.setTokenCookie(response, regenerateAccessToken, regenerateRefreshToken);

                    UserDetails userDetails = userService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    clearTokenCookies(response);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request, String tokenName) {
        Cookie accessTokenCookie = WebUtils.getCookie(request, tokenName);
        return accessTokenCookie != null ? accessTokenCookie.getValue() : null;
    }

    private void clearTokenCookies(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", "");
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("refreshToken", "");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

}
