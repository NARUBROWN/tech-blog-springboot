package com.naru.tech.data.repository;

import com.naru.tech.data.domain.Post;
import com.naru.tech.data.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findAllByPost(Post post);
}
