package com.naru.tech.data.repository;

import com.naru.tech.data.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
