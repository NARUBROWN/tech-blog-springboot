package com.scout.tech.data.dto.request;

public record UserRequest(
        String username,
        String email,
        String profileImageUrl,
        String bio,
        String originalPassword
) {
}
