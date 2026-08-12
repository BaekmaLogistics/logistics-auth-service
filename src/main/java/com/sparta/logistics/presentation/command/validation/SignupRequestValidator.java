package com.sparta.logistics.presentation.command.validation;

import com.sparta.logistics.presentation.command.request.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


//SignupRequest 역할별 필드 validator
//허브 관리자라면 hubId가 필수
//업체 관리자라면 companyId가 필수
//업체 배송 담당자라면 hubId가 필수
public class SignupRequestValidator implements ConstraintValidator<ValidSignupRequest, SignupRequest> {


  @Override
  public boolean isValid(SignupRequest request, ConstraintValidatorContext context) {

    if (request == null || request.requestedRole() == null) {
      return true;
    }

    context.disableDefaultConstraintViolation();

    return switch (request.requestedRole()) {
      case HUB_MANAGER -> validateHubManager(request, context);
      case DELIVERY_MANAGER -> validateDeliveryManager(request, context);
      case SUPPLIER_MANAGER -> validateSupplierManager(request, context);
    };
  }

  // 허브 관리자 검증
  private boolean validateHubManager(
      SignupRequest request,
      ConstraintValidatorContext context
  ) {
    boolean valid = true;

    if (request.hubId() == null) {
      addViolation(
          context,
          "hubId",
          "허브 관리자는 소속 허브 ID가 필수입니다."
      );

      valid = false;
    }

    if (request.companyId() != null) {
      addViolation(
          context,
          "companyId",
          "허브 관리자는 업체 ID를 입력할 수 없습니다."
      );
      valid = false;
    }

    if (request.deliveryManagerType() != null) {
      addViolation(
          context,
          "deliveryManagerType",
          "허브 관리자는 배송 담당자 타입을 입력할 수 없습니다."
      );
      valid = false;
    }

    return valid;
  }

  //배송 담당자 검증
  //HUB_DELIVERY와 COMPANY_DELIVERY
  private boolean validateDeliveryManager(
      SignupRequest request,
      ConstraintValidatorContext context
  ) {
    boolean valid = true;

    if (request.deliveryManagerType() == null) {
      addViolation(
          context,
          "deliveryManagerType",
          "배송 담당자 타입은 필수입니다."
      );
      return false;
    }

    if (request.companyId() != null) {
      addViolation(
          context,
          "companyId",
          "배송 담당자는 업체 ID를 입력할 수 없습니다."
      );
      valid = false;
    }
    switch (request.deliveryManagerType()) {
      case HUB_DELIVERY -> {
        if (request.hubId() != null) {
          addViolation(
              context,
              "hubId",
              "허브 배송 담당자는 특정 허브에 소속되지 않습니다."
          );
          valid = false;
        }
      }
      case COMPANY_DELIVERY -> {
        if (request.hubId() == null) {
          addViolation(
              context,
              "hubId",
              "업체 배송 담당자는 소속 허브 ID가 필수입니다."
          );
          valid = false;
        }
      }

    }
    return valid;
  }

  // 업체 관리자 검증
  private boolean validateSupplierManager(SignupRequest request, ConstraintValidatorContext context) {

    boolean valid = true;

    if (request.companyId() == null) {
      addViolation(
          context,
          "companyId",
          "업체 담당자는 소속 업체 ID가 필수입니다."
      );
      valid = false;
    }

    if (request.hubId() != null) {
      addViolation(
          context,
          "hubId",
          "업체 담당자는 허브 ID를 입력할 수 없습니다."
      );
      valid = false;
    }

    if (request.deliveryManagerType() != null) {
      addViolation(
          context,
          "deliveryManagerType",
          "업체 담당자는 배송 담당자 타입을 입력할 수 없습니다."
      );
      valid = false;
    }

    return valid;
  }

  // 특정 필드 검증 오류 등록
  private void addViolation(
      ConstraintValidatorContext context,
      String field,
      String message
  ) {
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(field)
        .addConstraintViolation();
  }

}
