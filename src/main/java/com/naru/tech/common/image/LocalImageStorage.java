package com.naru.tech.common.image;

import com.naru.tech.data.dto.service.StoredImage;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class LocalImageStorage implements ImageStorage{
    private final Path root;

    public LocalImageStorage(
            @Value("${image.upload-dir}")
            String uploadDir
    ) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    void init() throws IOException {
        Files.createDirectories(root);
    }

    @Override
    public StoredImage store(MultipartFile file) {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String storedName = UUID.randomUUID() + "." + ext;

        Path target = root.resolve(storedName).normalize();

        if (!target.startsWith(root)) {
            throw new SecurityException("Path Traversal 시도 감지");
        }

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }

        return StoredImage.builder()
                .storedName(storedName)
                .originalName(file.getOriginalFilename())
                .size(file.getSize())
                .contentType(file.getContentType())
                .build();
    }

    @Override
    public Resource load(String storedName) {
        Path path = root.resolve(storedName);
        return new FileSystemResource(path);
    }
}
