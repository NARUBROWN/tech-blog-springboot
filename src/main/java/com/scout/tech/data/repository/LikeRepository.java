package com.scout.tech.data.repository;

import com.scout.tech.data.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
