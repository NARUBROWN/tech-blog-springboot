package com.scout.tech.controller;

import com.scout.tech.data.dto.request.UserRequest;
import com.scout.tech.service.UserService;
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
    public ResponseEntity<Void> signup(@RequestBody UserRequest userRequest) {
        userService.createAuthorUser(userRequest);
        return ResponseEntity.ok().build();
    }
}
