package com.naru.tech.data.dto.service;

import org.springframework.core.io.Resource;

public record ResourceWithMeta(
        Resource resource,
        String contentType
) {
}
