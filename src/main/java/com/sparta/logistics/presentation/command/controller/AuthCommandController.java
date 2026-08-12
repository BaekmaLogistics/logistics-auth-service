package com.sparta.logistics.presentation.command.controller;

import com.sparta.logistics.application.command.dto.IssuedTokens;
import com.sparta.logistics.application.command.dto.LogoutCommand;
import com.sparta.logistics.application.command.dto.ReissueCommand;
import com.sparta.logistics.application.command.dto.response.CreateSignupResponse;
import com.sparta.logistics.application.command.dto.response.TokenResponse;
import com.sparta.logistics.application.command.usecase.LoginUseCase;
import com.sparta.logistics.application.command.usecase.LogoutUseCase;
import com.sparta.logistics.application.command.usecase.ReissueUseCase;
import com.sparta.logistics.application.command.usecase.SignupUseCase;
import com.sparta.logistics.infrastructure.security.cookie.RefreshTokenCookieManager;
import com.sparta.logistics.presentation.command.request.LoginRequest;
import com.sparta.logistics.presentation.command.request.SignupRequest;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import com.sparta.logistics.common.code.GeneralResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthCommandController {

  private final SignupUseCase signupUseCase;
  private final LoginUseCase loginUseCase;
  private final ReissueUseCase reissueUseCase;
  private final LogoutUseCase logoutUseCase;
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
  public ResponseEntity<GeneralResponse<TokenResponse>> login(
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
                TokenResponse.from(tokens)
            )
        );
  }

  @PostMapping("/reissue")
  public ResponseEntity<GeneralResponse<TokenResponse>> reissue(
      @CookieValue("refreshToken") String refreshToken
  ) {

    IssuedTokens reissueTokens = reissueUseCase.reissue(ReissueCommand.create(refreshToken));

    ResponseCookie responseCookie = refreshTokenCookieManager.create(
        reissueTokens.refreshToken(),
        reissueTokens.refreshTokenExpiresIn()
    );

    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE,
            responseCookie.toString()
        )
        .body(
            GeneralResponse.of(
                GeneralResponseCode.OK,
                TokenResponse.from(reissueTokens)
            )
        );
  }

  @PostMapping("/logout")
  public ResponseEntity<GeneralResponse<Void>> logout(
      @CookieValue(name = "refreshToken") String refreshToken
  ) {

    logoutUseCase.logout(LogoutCommand.create(refreshToken));

    ResponseCookie deleteCookie = refreshTokenCookieManager.delete();

    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE,
            deleteCookie.toString()
        )
        .body(
            GeneralResponse.<Void>of(
                GeneralResponseCode.OK,
                null
            ));
  }
}
