package com.sparta.logistics.application.command.service;

import com.sparta.logistics.application.command.dto.CreateSignupCommand;
import com.sparta.logistics.application.command.dto.CreateSignupResponse;
import com.sparta.logistics.application.command.usecase.SignupUseCase;
import com.sparta.logistics.domain.entity.AuthAccounts;
import com.sparta.logistics.domain.repository.AuthAccountsRepository;
import com.sparta.logistics.infrastructure.feign.client.user.UserServiceClient;
import com.sparta.logistics.infrastructure.feign.dto.request.CreatePendingUserRequest;
import com.sparta.logistics.presentation.common.dto.response.ErrorResponseCode;
import com.sparta.logistics.presentation.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthCommandService implements SignupUseCase {

  private final AuthAccountsRepository authAccountsRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserServiceClient userServiceClient;


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

  private void validateUsername(String username) {
    if (authAccountsRepository.existsByUsername(username)) {
      throw new ApiException(ErrorResponseCode.DUPLICATE_USERNAME);
    }
  }
}
