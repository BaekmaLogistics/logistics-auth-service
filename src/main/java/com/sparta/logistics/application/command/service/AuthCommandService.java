package com.sparta.logistics.application.command.service;

import com.sparta.logistics.application.command.dto.*;
import com.sparta.logistics.application.command.usecase.LoginUseCase;
import com.sparta.logistics.application.command.usecase.SignupUseCase;
import com.sparta.logistics.domain.entity.AuthAccounts;
import com.sparta.logistics.domain.repository.AuthAccountsRepository;
import com.sparta.logistics.domain.repository.RefreshTokenRepository;
import com.sparta.logistics.infrastructure.feign.client.user.UserServiceClient;
import com.sparta.logistics.infrastructure.feign.dto.request.CreatePendingUserRequest;
import com.sparta.logistics.infrastructure.security.CustomUserDetails;
import com.sparta.logistics.infrastructure.security.jwt.JwtTokenProvider;
import com.sparta.logistics.presentation.common.dto.response.ErrorResponseCode;
import com.sparta.logistics.presentation.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthCommandService implements SignupUseCase, LoginUseCase {

  private final AuthAccountsRepository authAccountsRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserServiceClient userServiceClient;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  // 회원가입 신청
  @Override
  public CreateSignupResponse createSignup(CreateSignupCommand command) {

    //username 중복 체크
    validateUsername(command.username());

    // 비밀번호 암호화
    String encodePassword = passwordEncoder.encode(command.password());

    // 승인 대기 상태의 인증 계정 생성
    AuthAccounts authAccounts = AuthAccounts.createPending(
        command.username(),
        encodePassword
    );

    AuthAccounts savedAuthAccount = authAccountsRepository.save(authAccounts);


    // UserService에 승인 대기 사용자 생성 요청
    userServiceClient.createPendingUser(
        new CreatePendingUserRequest(
            savedAuthAccount.getId(),
            command.name(),
            command.slackId(),
            command.requestedRole(),
            command.hubId(),
            command.companyId(),
            command.deliveryManagerType()
        )
    );

    return CreateSignupResponse.from(savedAuthAccount);
  }


  //로그인
  @Override
  public IssuedTokens login(LoginCommand command) {
    Authentication authenticate;
    try {
      authenticate = authenticationManager
          .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(
              command.username(),
              command.password()
          ));

    } catch (DisabledException e) {
      throw new ApiException(ErrorResponseCode.ACCOUNT_NOT_APPROVED);
    } catch (BadCredentialsException e) {
      throw new ApiException(ErrorResponseCode.INVALID_CREDENTIALS);
    }

    CustomUserDetails userDetails =
        (CustomUserDetails) authenticate.getPrincipal();

    // accessToken, refreshToken 생성
    IssuedTokens tokens = jwtTokenProvider.createToken(
        userDetails.getUserId(),
        userDetails.getUsername(),
        userDetails.getRole().toString()
    );

    //redis에 refreshToken 저장
    saveRefreshToken(
        userDetails.getUserId(),
        tokens.refreshToken(),
        tokens.refreshTokenExpiresIn()
    );
    return tokens;
  }

  private void saveRefreshToken(UUID userId, String refreshToken, long expiresIn) {
    refreshTokenRepository.save(
        userId,
        refreshToken,
        Duration.ofSeconds(expiresIn)
    );
  }

  private void validateUsername(String username) {
    if (authAccountsRepository.existsByUsername(username)) {
      throw new ApiException(ErrorResponseCode.DUPLICATE_USERNAME);
    }
  }
}
