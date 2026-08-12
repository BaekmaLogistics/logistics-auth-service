package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.IssuedTokens;
import com.sparta.logistics.application.command.dto.LoginCommand;

public interface LoginUseCase {
  IssuedTokens login(LoginCommand command);
}
