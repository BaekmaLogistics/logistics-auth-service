package com.sparta.logistics.application.command.dto.response;

import com.sparta.logistics.domain.entity.AuthAccounts;
import com.sparta.logistics.domain.model.AccountStatus;

import java.util.UUID;

public record CreateSignupResponse(
    UUID userId,
    String username,
    AccountStatus status
) {
  public static CreateSignupResponse from(AuthAccounts authAccounts) {
    return new CreateSignupResponse(
        authAccounts.getId(),
        authAccounts.getUsername(),
        authAccounts.getAccountStatus()
    );
  }

}
