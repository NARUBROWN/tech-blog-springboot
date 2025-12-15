package com.naru.tech.controller;

import com.naru.tech.data.domain.User;
import com.naru.tech.data.dto.request.LoginRequest;
import com.naru.tech.data.dto.response.LoginResponse;
import com.naru.tech.service.AuthService;
import com.naru.tech.service.LikeService;
import com.naru.tech.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@Tag(name = "[좋아요] 좋아요 API")
@RequiredArgsConstructor
public class LikeController {
    private final PostService postService;
    private final LikeService likeService;

    @PostMapping(path = "/{postId}")
    public ResponseEntity<Void> like(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ) {
        postService.likePost(postId, user.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<Void> unlike(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ) {
        postService.unlikePost(postId, user.getId());
        return ResponseEntity.ok().build();
    }

}
