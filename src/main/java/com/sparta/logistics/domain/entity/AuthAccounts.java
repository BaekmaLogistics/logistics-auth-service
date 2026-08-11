package com.sparta.logistics.domain.entity;

import com.sparta.logistics.domain.model.AccountStatus;
import com.sparta.logistics.domain.model.Role;
import com.sparta.logistics.infrastructure.persistence.jpa.entity.BaseUpdatableEntity;
import com.sparta.logistics.common.code.ErrorResponseCode;
import com.sparta.logistics.common.exception.ApiException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_auth_accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthAccounts extends BaseUpdatableEntity {

  @Column(nullable = false, unique = true, length = 10)
  private String username;

  @Column(nullable = false ,length = 255)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private AccountStatus accountStatus;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private Role role;

  private AuthAccounts(String username, String password) {
    this.username = username;
    this.password = password;
    this.accountStatus = AccountStatus.PENDING;
    this.role = null;
  }

  public static AuthAccounts createPending(String username, String password) {
    return new AuthAccounts(username, password);
  }

  public void activate(Role role) {

    if (role == null) {
      throw new ApiException(
          ErrorResponseCode.ACCOUNT_ACTIVATION_NOT_ALLOWED
      );
    }

    // PENDING 상태에서만 활성화 가능
    if (this.accountStatus != AccountStatus.PENDING) {
      throw new ApiException(
          ErrorResponseCode.ACCOUNT_ACTIVATION_NOT_ALLOWED
      );
    }

    this.role = role;
    this.accountStatus = AccountStatus.ACTIVE;

  }

  public void changePassword(String encodePassword) {
    this.password = encodePassword;
  }

  public void approve() {
    this.accountStatus = AccountStatus.ACTIVE;
  }

  public void reject() {
    this.accountStatus = AccountStatus.REJECTED;
  }

  public void disable() {
    this.accountStatus = AccountStatus.DISABLED;
  }

  public boolean isEnabled() {
    return this.accountStatus == AccountStatus.ACTIVE
        && this.getDeletedAt() == null;
  }
}
