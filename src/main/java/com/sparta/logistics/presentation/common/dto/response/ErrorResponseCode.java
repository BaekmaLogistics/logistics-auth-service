package com.sparta.logistics.presentation.common.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorResponseCode implements ApiResponseCode {
    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON_0001", "알 수 없는 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_0002","유효하지 않은 요청입니다."),
    FEIGN_CLIENT_ERROR(HttpStatus.BAD_GATEWAY, "COMMON_0003", "Feign 통신 중 오류가 발생했습니다."),

    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "AUTH_0001", "이미 사용중인 아이디입니다"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_0002", "아이디 또는 비밀번호가 올바르지 않습니다."),
    ACCOUNT_NOT_APPROVED(HttpStatus.FORBIDDEN, "AUTH_0003", "승인되지 않은 계정입니다." );



    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
