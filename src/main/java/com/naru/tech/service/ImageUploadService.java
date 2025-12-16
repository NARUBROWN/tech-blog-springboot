package com.naru.tech.service;

import com.naru.tech.common.exception.ImageMetadataNotFoundException;
import com.naru.tech.common.image.ImageStorage;
import com.naru.tech.common.image.ImageValidator;
import com.naru.tech.data.domain.ImageMetadata;
import com.naru.tech.data.dto.service.ResourceWithMeta;
import com.naru.tech.data.dto.service.StoredImage;
import com.naru.tech.data.dto.web.response.ImageUploadResponse;
import com.naru.tech.data.repository.ImageMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUploadService {
    private final ImageValidator validator;
    private final ImageStorage storage;
    private final ImageMetadataRepository repository;

    @Transactional
    public ImageUploadResponse upload(MultipartFile file) {
        validator.validate(file);

        StoredImage stored = storage.store(file);

        ImageMetadata saved = repository.save(
                ImageMetadata.fromDTO(stored)
        );

        return new ImageUploadResponse(
                saved.getId(),
                "/api/v1/image/" + saved.getPublicId()
        );
    }

    public ResourceWithMeta loadByPublicId(String uuid) {
        ImageMetadata imageMetadata = repository.findByPublicId(uuid).orElseThrow(ImageMetadataNotFoundException::new);
        String storedName = imageMetadata.getStoredName();
        return new ResourceWithMeta(
                storage.load(storedName),
                imageMetadata.getContentType()
        );
    }
}
