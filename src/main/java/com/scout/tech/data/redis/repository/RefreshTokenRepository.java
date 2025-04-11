package com.scout.tech.data.redis.repository;

import com.scout.tech.data.redis.object.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
