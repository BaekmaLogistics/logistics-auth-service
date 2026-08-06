package com.sparta.logistics.infrastructure.security;

import com.sparta.logistics.domain.entity.AuthAccounts;
import com.sparta.logistics.domain.repository.AuthAccountsRepository;
import com.sparta.logistics.presentation.common.dto.response.ErrorResponseCode;
import com.sparta.logistics.presentation.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AuthAccountsRepository authAccountsRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AuthAccounts authAccounts = authAccountsRepository.findByUsername(username)
        .orElseThrow(() ->
            new UsernameNotFoundException("인증 계정을 찾을 수 없습니다."
            )
        );

    return new CustomUserDetails(
        authAccounts.getId(),
        authAccounts.getUsername(),
        authAccounts.getPassword(),
        authAccounts.getRole(),
        authAccounts.isEnabled()
    );
  }

}
