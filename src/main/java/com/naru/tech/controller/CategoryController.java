package com.naru.tech.controller;

import com.naru.tech.data.dto.web.response.CategoryResponse;
import com.naru.tech.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "[카테고리] 카테고리 API")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createCategory(String name) {
        categoryService.createCategory(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        List<CategoryResponse> result = categoryService.getAllCategory();
        return ResponseEntity.ok(result);
    }
}
