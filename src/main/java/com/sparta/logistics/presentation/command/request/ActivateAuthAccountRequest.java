package com.sparta.logistics.presentation.command.request;

import com.sparta.logistics.application.command.dto.ActivateAuthAccountCommand;
import com.sparta.logistics.domain.model.Role;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ActivateAuthAccountRequest(

    @NotNull(message = "승인 역할은 필수입니다.")
    Role role
) {

  public ActivateAuthAccountCommand toCommand(UUID userId) {
    return new ActivateAuthAccountCommand(userId, role);
  }
}
