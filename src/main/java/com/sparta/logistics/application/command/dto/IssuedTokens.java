package com.sparta.logistics.application.command.dto;

public record IssuedTokens(
    String accessToken,
    String refreshToken,
    long accessTokenExpiresIn,
    long refreshTokenExpiresIn

) {
}
