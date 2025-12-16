package com.naru.tech.service;

import com.naru.tech.data.domain.Post;
import com.naru.tech.data.domain.PostTag;
import com.naru.tech.data.domain.Tag;
import com.naru.tech.data.dto.web.response.TagResponse;
import com.naru.tech.data.repository.PostTagRepository;
import com.naru.tech.data.repository.TagRepository;
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
        // DB에 존재하지 않는 태그 목록을 저장하기 위해 List 선언
        List<Tag> notExistTags = new ArrayList<>();

        // 사용자가 요청한 tags를 순회하면서, DB에 확인된 목록에 없다면 존재하지 않는 태그 목록에 추가함
        for (String tag : tags) {
            if (!tagNames.contains(tag)) {
                notExistTags.add(new Tag(tag));
            }
        }

        // DB에 존재하지 않는 TAG들을 DB에 저장
        List<Tag> newTags= tagRepository.saveAll(notExistTags);
        tagList.addAll(newTags);

        // Post와 Tag를 연결시켜주는 Entity 생성
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
