package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.IssuedTokens;
import com.sparta.logistics.application.command.dto.ReissueCommand;

public interface ReissueUseCase {

  IssuedTokens reissue(ReissueCommand command);
}
