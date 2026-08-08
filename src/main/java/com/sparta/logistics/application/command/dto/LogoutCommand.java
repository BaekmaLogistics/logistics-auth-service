package com.sparta.logistics.application.command.dto;

public record LogoutCommand(
    String refreshToken
) {

  public static LogoutCommand create(String refreshToken) {
    return new LogoutCommand(refreshToken);
  }

}
