package com.naru.tech.controller;

import com.naru.tech.data.domain.Post;
import com.naru.tech.data.dto.request.PostRequest;
import com.naru.tech.data.dto.response.PostResponse;
import com.naru.tech.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "[포스트] 포스트 API")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest, @RequestParam Long categoryId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        postService.createPost(postRequest, categoryId, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/slug/{slug}")
    public ResponseEntity<PostResponse> getPostBySlug(@PathVariable String slug) {
        PostResponse result = postService.getPostBySlug(slug);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse result = postService.getPostById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Page<PostResponse>> getAllPost(
            @Parameter(hidden = true)
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        Page<PostResponse> result = postService.getAllPost(pageable);
        return ResponseEntity.ok(result);
    }
}
