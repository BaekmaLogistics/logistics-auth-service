package com.sparta.logistics.domain.repository;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

  void save(UUID userId, String refreshToken, Duration expiration);

  Optional<String> findByUserId(UUID userId);

  void deleteByUserId(UUID userId);
}
