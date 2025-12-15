package com.naru.tech.service;

import com.naru.tech.data.domain.Category;
import com.naru.tech.data.dto.response.CategoryResponse;
import com.naru.tech.data.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 생성 메서드
     * @author 김원정
     * @param name 카테고리 이름
     */
    public void createCategory(String name) {
        Category category = new Category(name);
        categoryRepository.save(category);
    }

    /**
     * 카테고리 전체 목록 조회 메서드
     * @author 김원정
     */
    public List<CategoryResponse> getAllCategory() {
        return categoryRepository.findAll().stream().map(CategoryResponse::fromEntity).toList();
    }
}
