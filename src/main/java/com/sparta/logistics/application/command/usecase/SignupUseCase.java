package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.CreateSignupCommand;
import com.sparta.logistics.application.command.dto.CreateSignupResponse;

public interface SignupUseCase {

  CreateSignupResponse createSignup(CreateSignupCommand command);
}
