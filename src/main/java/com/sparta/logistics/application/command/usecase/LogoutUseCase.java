package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.LogoutCommand;

public interface LogoutUseCase {

  void logout(LogoutCommand command);
}
