package com.naru.tech.controller;

import com.naru.tech.data.dto.web.request.UserRequest;
import com.naru.tech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/author-signup")
    public ResponseEntity<Void> authorSignup(@RequestBody UserRequest userRequest) {
        userService.createAuthorUser(userRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/admin-signup")
    public ResponseEntity<Void> AdminSignup(@RequestBody UserRequest userRequest) {
        userService.createAdminUser(userRequest);
        return ResponseEntity.ok().build();
    }
}
