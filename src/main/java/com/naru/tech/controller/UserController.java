package com.naru.tech.controller;

import com.naru.tech.data.dto.web.request.UserRequest;
import com.naru.tech.data.dto.web.response.UserResponse;
import com.naru.tech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/normal-signup")
    public ResponseEntity<Void> NormalSignup(@RequestBody UserRequest userRequest) {
        userService.createNormalUser(userRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserResponse> updateUserInfo(
            @PathVariable Long id,
            @RequestBody UserRequest userRequest
    ) {
        UserResponse result = userService.updateUserInfo(id, userRequest);
        return ResponseEntity.ok(result);
    }
}
