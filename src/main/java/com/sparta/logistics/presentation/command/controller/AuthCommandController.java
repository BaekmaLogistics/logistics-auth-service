package com.sparta.logistics.presentation.command.controller;

import com.sparta.logistics.application.command.dto.CreateSignupResponse;
import com.sparta.logistics.application.command.dto.IssuedTokens;
import com.sparta.logistics.application.command.dto.LoginResponse;
import com.sparta.logistics.application.command.usecase.LoginUseCase;
import com.sparta.logistics.application.command.usecase.SignupUseCase;
import com.sparta.logistics.infrastructure.security.cookie.RefreshTokenCookieManager;
import com.sparta.logistics.presentation.command.request.LoginRequest;
import com.sparta.logistics.presentation.command.request.SignupRequest;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
  private final LoginUseCase loginUseCase;
  private final RefreshTokenCookieManager refreshTokenCookieManager;

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

  @PostMapping("/login")
  public ResponseEntity<GeneralResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request
  ) {
    IssuedTokens tokens = loginUseCase.login(request.toCommand());

    ResponseCookie responseCookie = refreshTokenCookieManager.create(
        tokens.refreshToken(),
        tokens.refreshTokenExpiresIn()
    );

    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE,
            responseCookie.toString()
        )
        .body(
            GeneralResponse.of(
                GeneralResponseCode.OK,
                LoginResponse.from(tokens)
            )
        );
  }

}
