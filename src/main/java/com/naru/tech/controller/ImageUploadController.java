package com.naru.tech.controller;

import com.naru.tech.data.dto.service.ResourceWithMeta;
import com.naru.tech.data.dto.web.response.ImageUploadResponse;
import com.naru.tech.service.ImageUploadService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
@Tag(name = "[이미지] 이미지 API")
public class ImageUploadController {
    private final ImageUploadService imageUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> upload(
            @RequestParam("file")
            MultipartFile file
    ) {
        ImageUploadResponse result = imageUploadService.upload(file);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/{uuid}")
    public ResponseEntity<Resource> getImage(@PathVariable String uuid) {
        ResourceWithMeta result = imageUploadService.loadByPublicId(uuid);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.contentType()))
                .body(result.resource());
    }
}
