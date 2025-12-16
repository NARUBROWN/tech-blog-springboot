package com.naru.tech.data.dto.service;

import lombok.Builder;

@Builder
public record StoredImage(
        String storedName,
        String originalName,
        long size,
        String contentType
) {
}
