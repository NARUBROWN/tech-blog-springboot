package com.naru.tech.data.dto.response;

import com.naru.tech.data.domain.Post;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostResponse(
        Long id,
        String title,
        String content,
        LocalDateTime publishedAt,
        Long likeCount,
        Long viewCount,
        String seoTitle,
        String seoDescription,
        String seoKeywords,
        UserResponse author,
        TagResponse tags,
        CategoryResponse category
) {
    public static PostResponse fromEntity(Post post, UserResponse author, TagResponse tags, CategoryResponse category) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .publishedAt(post.getPublishedAt())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .seoTitle(post.getSeoTitle())
                .seoDescription(post.getSeoDescription())
                .seoKeywords(post.getSeoKeywords())
                .author(author)
                .tags(tags)
                .category(category)
                .build();
    }
}
