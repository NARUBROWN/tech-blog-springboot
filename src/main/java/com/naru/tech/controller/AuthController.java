package com.naru.tech.controller;

import com.naru.tech.data.dto.web.request.LoginRequest;
import com.naru.tech.data.dto.web.response.LoginResponse;
import com.naru.tech.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse response, @RequestBody LoginRequest loginRequest) {
        LoginResponse result = authService.login(response, loginRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok().build();
    }

}
