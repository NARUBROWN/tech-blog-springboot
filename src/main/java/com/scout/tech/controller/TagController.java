package com.scout.tech.controller;

import com.scout.tech.service.TagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
@Tag(name = "[태그] 태그 API")
public class TagController {
    private final TagService tagService;
}
