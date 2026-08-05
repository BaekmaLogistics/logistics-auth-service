package com.sparta.logistics.application.command.dto;

import com.sparta.logistics.domain.model.DeliveryManagerType;
import com.sparta.logistics.domain.model.RequestedRole;

import java.util.UUID;

public record CreateSignupCommand(
    String username,
    String password,
    String name,
    String slackId,
    RequestedRole requestedRole,
    UUID hubId,
    UUID companyId,
    DeliveryManagerType deliveryManagerType
) {
}
