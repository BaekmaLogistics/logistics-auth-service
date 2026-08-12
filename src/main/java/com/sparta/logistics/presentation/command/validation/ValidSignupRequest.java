package com.sparta.logistics.presentation.command.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SignupRequestValidator.class)
public @interface ValidSignupRequest {

  String message() default "회원가입 요청 정보가 올바르지 않습니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


}
