package com.naru.tech.data.repository;

import com.naru.tech.data.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByCategory_Name(String categoryName, Pageable pageable);

    @Modifying
    @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :id")
    void incrementViewCount(@Param("id") Long id);

    @Query("""
        select distinct p
        from Post p
        left join PostTag pt on pt.post = p
        left join pt.tag t
        where
            lower(p.title) like lower(concat('%', :keyword, '%')) or
            p.content like concat('%', :keyword, '%') or
            lower(p.seoTitle) like lower(concat('%', :keyword, '%')) or
            lower(p.seoDescription) like lower(concat('%', :keyword, '%')) or
            lower(t.name) like lower(concat('%', :keyword, '%'))
        order by p.publishedAt desc
    """)
    Page<Post> searchPost(@Param("keyword") String keyword, Pageable pageable);
}
