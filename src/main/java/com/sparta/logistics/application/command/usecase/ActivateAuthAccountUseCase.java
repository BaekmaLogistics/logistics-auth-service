package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.ActivateAuthAccountCommand;

public interface ActivateAuthAccountUseCase {

  void activateAccount(ActivateAuthAccountCommand command);
}
