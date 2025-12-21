package com.naru.tech.service;

import com.naru.tech.common.exception.CategoryNotFoundException;
import com.naru.tech.common.exception.LikeNotFoundException;
import com.naru.tech.common.exception.PostNotFoundException;
import com.naru.tech.common.exception.UserNotFoundException;
import com.naru.tech.data.domain.Category;
import com.naru.tech.data.domain.Like;
import com.naru.tech.data.domain.Post;
import com.naru.tech.data.domain.User;
import com.naru.tech.data.dto.web.request.PostRequest;
import com.naru.tech.data.dto.web.response.CategoryResponse;
import com.naru.tech.data.dto.web.response.PostResponse;
import com.naru.tech.data.dto.web.response.TagResponse;
import com.naru.tech.data.dto.web.response.UserResponse;
import com.naru.tech.data.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostTagService postTagService;
    private final LikeRepository likeRepository;

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

    public Page<PostResponse> getPost(String categoryName, Pageable pageable) {
        if (categoryName != null) {
            return postRepository.findAllByCategory_Name(categoryName, pageable)
                    .map(post -> PostResponse.fromEntity(
                            post,
                            UserResponse.fromEntity(post.getUser()),
                            postTagService.findTagListByPost(post),
                            CategoryResponse.fromEntity(post.getCategory())
                    ));
        }
        return postRepository.findAll(pageable)
                .map(post -> PostResponse.fromEntity(
                        post,
                        UserResponse.fromEntity(post.getUser()),
                        postTagService.findTagListByPost(post),
                        CategoryResponse.fromEntity(post.getCategory())
                ));
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        // 태그 삭제
        postTagService.deletePostTagRelation(post);
        // 좋아요 기록 삭제
        likeRepository.deleteAllByPost(post);
        // 포스트 삭제
        postRepository.delete(post);
    }

    @Transactional
    public void likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        }

        Like like = new Like(post, user);
        likeRepository.save(like);

        post.increaseLike();
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Like like = likeRepository.findByPostIdAndUserId(postId, userId).orElseThrow(LikeNotFoundException::new);

        Post post = like.getPost();
        likeRepository.delete(like);

        post.decreaseLike();
    }

    @Transactional
    public void incrementViewCount(Long postId) {
        postRepository.incrementViewCount(postId);
    }
}
