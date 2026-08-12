package com.sparta.logistics.application.command.dto;

import com.sparta.logistics.domain.model.Role;

import java.util.UUID;

public record ActivateAuthAccountCommand(
    UUID userId,
    Role role
) {

}
