package com.naru.tech.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/image",
            "/api/v1/image/**",
            "/api/v1/post/slug/*",
            "/api/v1/post/id/*",
            "/api/v1/post/all",
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/api/v1/user/normal-signup",
            "/api/v1/user/author-signup",
            "/api/v1/user/admin-signup",
            "/api/v1/auth/login"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->
                        authorize
                                // 최신순 키워드 검색 전체 공개
                                .requestMatchers(HttpMethod.GET, "/api/v1/post/search/recent").permitAll()
                                // 조회수 증가 카운트 전체 공개
                                .requestMatchers(HttpMethod.POST, "/api/v1/post/view-count/**").permitAll()
                                // 카테고리 목록 조회 전체 공개
                                .requestMatchers(HttpMethod.GET, "/api/v1/category/list").permitAll()
                                // 게시글 목록 조회 전체 공개
                                .requestMatchers(HttpMethod.GET, "/api/v1/post").permitAll()
                                // 게시글 세부 조회 전체 공개
                                .requestMatchers(HttpMethod.GET, "/api/v1/post/**").permitAll()
                                // 좋아요 누른 사람들 목록 전체 공개
                                .requestMatchers(HttpMethod.GET, "/api/v1/like/**").permitAll()
                                // 나머지 기존 화이트 리스트
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .anyRequest().authenticated())
                .securityContext(context -> context
                        .requireExplicitSave(false)
                        .securityContextRepository(new NullSecurityContextRepository()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://na2ru2.me", "https://na2ru2.me", "https://www.na2ru2.me", "http://www.na2ru2.me"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
