package com.sparta.logistics.presentation.command.controller;

import com.sparta.logistics.application.command.dto.RejectAuthAccountCommand;
import com.sparta.logistics.application.command.usecase.ActivateAuthAccountUseCase;
import com.sparta.logistics.application.command.usecase.RejectAuthAccountUseCase;
import com.sparta.logistics.presentation.command.request.ActivateAuthAccountRequest;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import com.sparta.logistics.common.code.GeneralResponseCode;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/auth/accounts")
public class InternalAUthCommandController {

  private final ActivateAuthAccountUseCase activateAuthAccountUseCase;
  private final RejectAuthAccountUseCase rejectAuthAccountUseCase;

  @SecurityRequirements
  @PatchMapping("/{userId}/activate")
  public ResponseEntity<GeneralResponse<Void>> activateAccount(
      @PathVariable UUID userId,
      @Valid @RequestBody ActivateAuthAccountRequest request
  ) {

    activateAuthAccountUseCase.activateAccount(request.toCommand(userId));

    return GeneralResponse.toResponseEntity(
        GeneralResponseCode.OK,
        null
    );
  }

  @SecurityRequirements
  @PatchMapping("/{userId}/reject")
  public ResponseEntity<GeneralResponse<Void>> rejectAccount(
      @PathVariable UUID userId
  ) {
    rejectAuthAccountUseCase.rejectAccount(new RejectAuthAccountCommand(userId));

    return GeneralResponse.toResponseEntity(
        GeneralResponseCode.OK,
        null
    );
  }
}
