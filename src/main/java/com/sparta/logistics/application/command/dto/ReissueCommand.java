package com.sparta.logistics.application.command.dto;

public record ReissueCommand(String refreshToken) {

  public static ReissueCommand create(String refreshToken) {
    return new ReissueCommand(refreshToken);
  }
}
