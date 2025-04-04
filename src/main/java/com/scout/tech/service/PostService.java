package com.scout.tech.service;

import com.scout.tech.common.exception.CategoryNotFoundException;
import com.scout.tech.common.exception.PostNotFoundException;
import com.scout.tech.common.exception.UserNotFoundException;
import com.scout.tech.data.domain.Category;
import com.scout.tech.data.domain.Post;
import com.scout.tech.data.domain.User;
import com.scout.tech.data.dto.request.PostRequest;
import com.scout.tech.data.dto.response.PostResponse;
import com.scout.tech.data.dto.response.UserResponse;
import com.scout.tech.data.repository.CategoryRepository;
import com.scout.tech.data.repository.PostRepository;
import com.scout.tech.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    /**
     *
     * @param postRequest
     * @param categoryId
     * @param userId
     */
    public void createPost(PostRequest postRequest, Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
        User user = userRepository.findById(userId).orElseThrow(() -> new CategoryNotFoundException(userId));

        LocalDateTime now = LocalDateTime.now();

        Post post = Post.builder()
                .title(postRequest.title())
                .slug(postRequest.title().trim().replaceAll("\\s+", "-"))
                .content(postRequest.content())
                .publishedAt(now)
                .thumbnailUrl(postRequest.thumbnailUrl())
                .likeCount(0L)
                .viewCount(0L)
                .seoTitle(postRequest.seoTitle())
                .seoDescription(postRequest.seoDescription())
                .seoKeywords(postRequest.seoKeywords())
                .category(category)
                .user(user)
                .build();

        postRepository.save(post);
    }

    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug).orElseThrow(() -> new PostNotFoundException(slug));
        User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new UserNotFoundException(post.getUser().getId()));

        UserResponse userResponse = UserResponse.fromEntity(user);
        return PostResponse.fromEntity(post, userResponse);
    }
}
