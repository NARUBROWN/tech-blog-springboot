package com.naru.tech.common.image;

import com.naru.tech.data.dto.service.StoredImage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {
    StoredImage store(MultipartFile file);
    Resource load(String storedName);
}
