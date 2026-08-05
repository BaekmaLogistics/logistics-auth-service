package com.sparta.logistics.presentation.command.controller;

import com.sparta.logistics.application.command.dto.CreateSignupResponse;
import com.sparta.logistics.application.command.usecase.SignupUseCase;
import com.sparta.logistics.presentation.command.request.SignupRequest;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthCommandController {

  private final SignupUseCase signupUseCase;

  @PostMapping("/signup")
  public ResponseEntity<GeneralResponse<CreateSignupResponse>> signup(
      @Valid @RequestBody SignupRequest request
  ) {
    CreateSignupResponse response = signupUseCase.createSignup(request.toCommand());

    return GeneralResponse.toResponseEntity(
        GeneralResponseCode.CREATED,
        response
    );
  }
}
