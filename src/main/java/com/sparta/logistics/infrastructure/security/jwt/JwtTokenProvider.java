package com.sparta.logistics.infrastructure.security.jwt;

import com.sparta.logistics.application.command.dto.IssuedTokens;
import com.sparta.logistics.common.code.ErrorResponseCode;
import com.sparta.logistics.common.exception.ApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
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
  private final JwtParser jwtParser;

  //서명 키와 토큰 파서 생성
  public JwtTokenProvider(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.key = Keys.hmacShaKeyFor(
        Decoders.BASE64.decode(jwtProperties.secretKey())
    );
    this.jwtParser = Jwts.parser()
        .verifyWith(key)
        .build();
  }


  //AccessToken RefreshToken 발급
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

  //RefreshToken 검증 및 사용자 ID반환
  public UUID parseRefreshToken(String refreshToken) {
    Claims claims = parserClaims(refreshToken);
    validateRefreshTokenType(claims);

    return UUID.fromString(claims.getSubject());
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

  // 토큰 서명 만료 여부 검증
  private Claims parserClaims(String token) {
    if (!StringUtils.hasText(token)) {
      throw new ApiException(
          ErrorResponseCode.INVALID_REFRESH_TOKEN
      );
    }

    Claims claims;
    try {
      claims = jwtParser
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException e) {
      throw new ApiException(
          ErrorResponseCode.EXPIRED_REFRESH_TOKEN
      );
    } catch (JwtException e) {
      throw new ApiException(
          ErrorResponseCode.INVALID_REFRESH_TOKEN
      );
    }

    return claims;
  }

  // RefreshToken인지 확인
  private void validateRefreshTokenType (Claims claims){
    String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);

    if (!REFRESH_TOKEN_TYPE.equals(tokenType)) {
      throw new ApiException(ErrorResponseCode.INVALID_REFRESH_TOKEN);
    }
  }

  // 만료 시간 생성
  private Date createExpiration (Instant issuedAt, Duration expiration){
    return Date.from(issuedAt.plus(expiration));
  }

}
