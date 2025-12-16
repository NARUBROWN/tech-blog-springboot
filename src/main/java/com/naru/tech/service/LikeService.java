package com.naru.tech.service;

import com.naru.tech.data.domain.Like;
import com.naru.tech.data.dto.web.response.UserResponse;
import com.naru.tech.data.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
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
