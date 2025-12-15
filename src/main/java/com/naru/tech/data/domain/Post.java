package com.naru.tech.data.domain;

import com.naru.tech.common.data.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "slug", length = 200)
    private String slug;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "seo_title", length = 200)
    private String seoTitle;

    @Column(name = "seo_description", length = 300)
    private String seoDescription;

    @Column(name = "seo_keywords", length = 300)
    private String seoKeywords;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(String title, String slug, String content, LocalDateTime publishedAt, String thumbnailUrl,
                Long likeCount, Long viewCount, String seoTitle, String seoDescription, String seoKeywords, Category category,
                User user) {
        this.title = title;
        this.slug = slug;
        this.content = content;
        this.publishedAt = publishedAt;
        this.thumbnailUrl = thumbnailUrl;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.seoKeywords = seoKeywords;
        this.category = category;
        this.user = user;
    }

    public void increaseLike() {
        this.likeCount++;
    }

    public void decreaseLike() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

}
