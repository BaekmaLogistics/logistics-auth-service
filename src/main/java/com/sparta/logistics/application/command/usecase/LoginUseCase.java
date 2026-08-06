package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.IssuedTokens;
import com.sparta.logistics.application.command.dto.LoginCommand;
import com.sparta.logistics.application.command.dto.LoginResponse;

public interface LoginUseCase {
  IssuedTokens login(LoginCommand command);
}
