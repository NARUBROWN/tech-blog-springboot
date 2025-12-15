package com.naru.tech.data.repository;

import com.naru.tech.data.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByCategory_Name(String categoryName, Pageable pageable);
}
