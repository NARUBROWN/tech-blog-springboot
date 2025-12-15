package com.naru.tech.data.dto.response;

import com.naru.tech.data.domain.Category;

public record CategoryResponse(
        Long id,
        String name
) {
    public static CategoryResponse fromEntity(Category category)  {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
