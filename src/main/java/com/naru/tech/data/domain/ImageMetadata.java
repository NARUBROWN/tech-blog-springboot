package com.naru.tech.data.domain;

import com.naru.tech.data.dto.service.StoredImage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    private String publicId = UUID.randomUUID().toString();

    @Column(name = "stored_name")
    private String storedName;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "size")
    private Long size;

    @Builder
    public ImageMetadata(String storedName, String originalName, String contentType, Long size) {
        this.storedName = storedName;
        this.originalName = originalName;
        this.contentType = contentType;
        this.size = size;
    }

    public static ImageMetadata fromDTO(StoredImage dto) {
        return ImageMetadata.builder()
                .storedName(dto.storedName())
                .originalName(dto.originalName())
                .contentType(dto.contentType())
                .size(dto.size())
                .build();
    }
}
