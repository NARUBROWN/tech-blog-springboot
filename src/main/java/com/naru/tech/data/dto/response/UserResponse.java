package com.naru.tech.data.dto.response;

import com.naru.tech.data.domain.User;
import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String username,
        String email,
        String profileImageUrl,
        String bio
) {
    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .build();
    }
}
