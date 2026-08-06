package com.sparta.logistics.infrastructure.security.cookie;

import com.sparta.logistics.infrastructure.security.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieManager {

  private static final String COOKIE_NAME = "refreshToken";
  private static final String COOKIE_PATH = "/api/v1/auth/reissue";


  public ResponseCookie create(String refreshToken, long expiresInSeconds) {
    return ResponseCookie.from(COOKIE_NAME, refreshToken)
        .httpOnly(true)
        .secure(false)
        .path(COOKIE_PATH)
        .maxAge(Duration.ofSeconds(expiresInSeconds))
        .build();
  }
}
