package com.scout.tech.service;

import com.scout.tech.data.domain.Post;
import com.scout.tech.data.domain.PostTag;
import com.scout.tech.data.domain.Tag;
import com.scout.tech.data.dto.response.TagResponse;
import com.scout.tech.data.repository.PostTagRepository;
import com.scout.tech.data.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostTagService {
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public void createPostTagRelation(Post post, List<String> tags) {
        // Post를 저장할때 전달된 태그 리스트로 찾은 Tags
        List<Tag> tagList = tagRepository.findAllByNameIn(tags);
        // DB에서 확인된 tagNames 목록을 String으로 저장함
        List<String> tagNames = tagList.stream().map(Tag::getName).toList();
        List<String> notExistTags = new ArrayList<>();

        for (String tag : tags) {
            if (!tagNames.contains(tag)) {
                notExistTags.add(tag);
            }
        }

        List<Tag> notExistTagList = new ArrayList<>();

        List<PostTag> postTagList = new ArrayList<>();
        for (Tag tag : tagList) {
            postTagList.add(new PostTag(post, tag));
        }

        postTagRepository.saveAll(postTagList);
    }

    public TagResponse findTagListByPost(Post post) {
        List<String> tagNames = postTagRepository.findAllByPost(post).stream().map(PostTag::getTag).map(Tag::getName).toList();
        return new TagResponse(tagNames);
    }
}
