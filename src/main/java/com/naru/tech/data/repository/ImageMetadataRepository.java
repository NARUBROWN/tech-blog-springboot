package com.naru.tech.data.repository;

import com.naru.tech.data.domain.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {
    Optional<ImageMetadata> findByPublicId(String publicId);
}
