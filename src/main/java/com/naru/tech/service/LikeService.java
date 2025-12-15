package com.naru.tech.service;

import com.naru.tech.common.enums.ErrorCode;
import com.naru.tech.common.exception.LoginException;
import com.naru.tech.config.JwtProvider;
import com.naru.tech.data.domain.Like;
import com.naru.tech.data.domain.User;
import com.naru.tech.data.dto.request.LoginRequest;
import com.naru.tech.data.dto.response.LoginResponse;
import com.naru.tech.data.dto.response.UserResponse;
import com.naru.tech.data.repository.LikeRepository;
import com.naru.tech.data.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public List<UserResponse> findUsersWhoLikedPost(Long postId) {
        List<Like> likes = likeRepository.findAllByPost_Id(postId);
        return likes.stream().map(like -> UserResponse.fromEntity(like.getUser())).toList();
    }
}
