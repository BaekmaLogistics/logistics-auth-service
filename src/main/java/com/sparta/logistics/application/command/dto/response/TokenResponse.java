package com.sparta.logistics.application.command.dto.response;

import com.sparta.logistics.application.command.dto.IssuedTokens;

public record TokenResponse(
    String tokenType,
    String accessToken,
    long expiresIn

) {

  public static TokenResponse from(IssuedTokens tokens) {
    return new TokenResponse(
        "Bearer",
        tokens.accessToken(),
        tokens.accessTokenExpiresIn()
    );
  }
}
