package com.naru.tech.controller;

import com.naru.tech.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "[카테고리] 카테고리 API")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public void createCategory(String name) {
        categoryService.createCategory(name);
    }
}
