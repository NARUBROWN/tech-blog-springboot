package com.naru.tech.data.dto.web.request;

public record PostRequest(
        String title,
        String content,
        String thumbnailUrl,
        String seoTitle,
        String seoDescription,
        String seoKeywords,
        TagRequest tags
) {
}
