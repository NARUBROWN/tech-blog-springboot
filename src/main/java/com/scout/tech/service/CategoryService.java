package com.scout.tech.service;

import com.scout.tech.data.domain.Category;
import com.scout.tech.data.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
