package com.naru.tech.data.dto.web.response;

import com.naru.tech.data.domain.User;

public record LoginResponse(
        String username,
        String email,
        String profileImageUrl,
        String role
) {
    public static LoginResponse fromEntity(User user) {
        return new LoginResponse(user.getUsername(), user.getEmail(), user.getProfileImageUrl(), user.getRole().name());
    }
}
