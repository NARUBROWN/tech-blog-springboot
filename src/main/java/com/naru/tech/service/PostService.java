package com.naru.tech.service;

import com.naru.tech.common.exception.CategoryNotFoundException;
import com.naru.tech.common.exception.PostNotFoundException;
import com.naru.tech.common.exception.UserNotFoundException;
import com.naru.tech.data.domain.Category;
import com.naru.tech.data.domain.Post;
import com.naru.tech.data.domain.User;
import com.naru.tech.data.dto.request.PostRequest;
import com.naru.tech.data.dto.response.CategoryResponse;
import com.naru.tech.data.dto.response.PostResponse;
import com.naru.tech.data.dto.response.TagResponse;
import com.naru.tech.data.dto.response.UserResponse;
import com.naru.tech.data.repository.CategoryRepository;
import com.naru.tech.data.repository.PostRepository;
import com.naru.tech.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostTagService postTagService;

    /**
     *
     * @param postRequest
     * @param categoryId
     * @param username
     */
    public void createPost(PostRequest postRequest, Long categoryId, String username) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));


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

        Post savedPost = postRepository.save(post);

        postTagService.createPostTagRelation(savedPost, postRequest.tags().tagNames());
    }

    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug).orElseThrow(() -> new PostNotFoundException(slug));

        UserResponse userResponse = UserResponse.fromEntity(post.getUser());
        TagResponse tagResponse = postTagService.findTagListByPost(post);
        CategoryResponse categoryResponse = CategoryResponse.fromEntity(post.getCategory());
        return PostResponse.fromEntity(post, userResponse, tagResponse, categoryResponse);
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));

        UserResponse userResponse = UserResponse.fromEntity(post.getUser());
        TagResponse tagResponse = postTagService.findTagListByPost(post);
        CategoryResponse categoryResponse = CategoryResponse.fromEntity(post.getCategory());

        return PostResponse.fromEntity(post, userResponse, tagResponse, categoryResponse);
    }

    public Page<PostResponse> getAllPost(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(post -> PostResponse.fromEntity(
                        post,
                        UserResponse.fromEntity(post.getUser()),
                        postTagService.findTagListByPost(post),
                        CategoryResponse.fromEntity(post.getCategory())
                ));
    }
}
