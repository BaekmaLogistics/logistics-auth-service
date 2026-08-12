package com.sparta.logistics.presentation.command.request;

import com.sparta.logistics.application.command.dto.CreateSignupCommand;
import com.sparta.logistics.domain.model.DeliveryManagerType;
import com.sparta.logistics.domain.model.RequestedRole;
import com.sparta.logistics.presentation.command.validation.ValidSignupRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;


@ValidSignupRequest
public record SignupRequest(
    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(
        regexp = "^[a-z0-9]{4,10}$",
        message = "아이디는 4자 이상 10자 이하의 영문 소문자와 숫자로 구성해야 합니다."
    )
    String username,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)"
            + "(?=.*[^A-Za-z0-9\\s])\\S{8,15}$",
        message = "비밀번호는 8자 이상 15자 이하의 영문, 숫자, 특수문자로 구성해야 합니다."
    )
    String password,

    @NotBlank(message = "이름은 필수입니다.")
    String name,

    @NotBlank(message = "Slack ID는 필수입니다.")
    String slackId,

    @NotNull(message = "신청 역할은 필수입니다.")
    RequestedRole requestedRole,

    UUID hubId,

    UUID companyId,

    DeliveryManagerType deliveryManagerType
    ) {

    public CreateSignupCommand toCommand() {
        return new CreateSignupCommand(
            username,
            password,
            name,
            slackId,
            requestedRole,
            hubId,
            companyId,
            deliveryManagerType
        );

    }


}
