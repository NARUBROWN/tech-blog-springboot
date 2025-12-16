package com.naru.tech.data.dto.web.response;

public record ImageUploadResponse(
        Long savedId,
        String url
) {
}
