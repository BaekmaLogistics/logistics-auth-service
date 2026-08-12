package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.RejectAuthAccountCommand;

public interface RejectAuthAccountUseCase {

  void rejectAccount(RejectAuthAccountCommand command);
}
