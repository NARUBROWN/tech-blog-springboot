package com.naru.tech.common.image;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class ImageValidator {
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/png",
            "image/jpeg",
            "image/webp"
    );

    private static final long MAX_SIZE = 1024 * 1024 * 20; // 20MB

    public void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("이미지 용량 초과");
        }

        if(!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("허용되지 않은 이미지 타입");
        }
    }
}
