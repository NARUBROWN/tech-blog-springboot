package com.naru.tech.service;

import com.naru.tech.data.domain.Tag;
import com.naru.tech.data.dto.response.TagResponse;
import com.naru.tech.data.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    /**
     * 태그를 생성할 때 사용하는 메서드
     * @param name 태그의 이름
     */
    public void createTag(String name) {
        Tag tag = new Tag(name);
        tagRepository.save(tag);
    }

    public TagResponse searchTagByName(String name) {
        List<String> tagNames = tagRepository.findByNameContaining(name).stream().map(Tag::getName).toList();
        return new TagResponse(tagNames);
    }
}
