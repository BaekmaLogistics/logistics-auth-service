package com.sparta.logistics.infrastructure.cache;

import com.sparta.logistics.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.data.redis.host")
public class RefreshTokenRedisRepository implements RefreshTokenRepository {

  private static final String KEY_PREFIX = "refresh-token:";
  private final StringRedisTemplate redisTemplate;

  @Value("${spring.application.name}")
  private String serviceName;

  @Override
  public void save(UUID userId, String refreshToken, Duration expiration) {
    redisTemplate.opsForValue().set(
        createKey(userId),
        refreshToken,
        expiration
    );
  }

  @Override
  public Optional<String> findByUserId(UUID userId) {
    return Optional.ofNullable(
        redisTemplate.opsForValue().get(createKey(userId))
    );
  }

  private String createKey(UUID userId) {
    return serviceName
        + "::"
        + KEY_PREFIX
        + userId;
  }
}
