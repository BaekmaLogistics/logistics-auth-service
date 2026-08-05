package com.sparta.logistics.infrastructure.feign.dto.request;

import com.sparta.logistics.domain.model.DeliveryManagerType;
import com.sparta.logistics.domain.model.RequestedRole;

import java.util.UUID;

public record CreatePendingUserRequest(
    UUID userId,
    String name,
    String slackId,
    RequestedRole requestedRole,
    UUID hubId,
    UUID companyId,
    DeliveryManagerType deliveryManagerType) {
}
