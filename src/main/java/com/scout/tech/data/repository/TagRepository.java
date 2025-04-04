package com.scout.tech.data.repository;

import com.scout.tech.data.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByNameIn(List<String> names);
    List<Tag> findByNameContaining(String name);
}
