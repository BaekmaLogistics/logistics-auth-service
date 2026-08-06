package com.sparta.logistics.infrastructure.security.jwt;

import com.sparta.logistics.application.command.dto.IssuedTokens;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

  private static final String TOKEN_TYPE_CLAIM = "tokenType";
  private static final String ACCESS_TOKEN_TYPE = "ACCESS";
  private static final String REFRESH_TOKEN_TYPE = "REFRESH";

  private final JwtProperties jwtProperties;
  private final SecretKey key;

  public JwtTokenProvider(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.key = Keys.hmacShaKeyFor(
        Decoders.BASE64.decode(jwtProperties.secretKey())
    );
  }

  public IssuedTokens createToken(UUID userId, String username, String role) {
    String accessToken = createAccessToken(userId, username, role);
    String refreshToken = createRefreshToken(userId);

    return new IssuedTokens(
        accessToken,
        refreshToken,
        jwtProperties.accessTokenExpiration().toSeconds(),
        jwtProperties.refreshTokenExpiration().toSeconds()
    );
  }


  //AccessToken 생성
  private String createAccessToken(UUID userId, String username, String role) {
    Instant issuedAt = Instant.now();

    return Jwts.builder()
        .subject(userId.toString())
        .claim("role", role)
        .claim("username", username)
        .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
        .issuedAt(Date.from(issuedAt))
        .expiration(createExpiration(issuedAt, jwtProperties.accessTokenExpiration()))
        .signWith(key)
        .compact();
  }

  //RefreshToken 생성
  private String createRefreshToken(UUID userId) {
    Instant issuedAt = Instant.now();

    return Jwts.builder()
        .subject(userId.toString())
        .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
        .issuedAt(Date.from(issuedAt))
        .expiration(createExpiration(issuedAt, jwtProperties.refreshTokenExpiration())
        )
        .signWith(key)
        .compact();
    
  }



  private Date createExpiration(Instant issuedAt, Duration expiration) {
    return Date.from(issuedAt.plus(expiration));
  }
}
