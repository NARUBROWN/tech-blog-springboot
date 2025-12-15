package com.naru.tech.data.redis.object;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("refreshToken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private Long userId;

    private String token;

    @TimeToLive
    private Long ttl;

    @Builder
    public RefreshToken(Long userId, String token, Long ttl) {
        this.userId = userId;
        this.token = token;
        this.ttl = ttl;
    }
}
