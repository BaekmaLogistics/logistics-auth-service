package com.sparta.logistics.application.command.dto;

public record LoginResponse(
    String tokenType,
    String accessToken,
    long expiresIn

) {

  public static LoginResponse from(IssuedTokens tokens) {
    return new LoginResponse(
        "Bearer",
        tokens.accessToken(),
        tokens.accessTokenExpiresIn()
    );
  }
}
