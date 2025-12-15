package com.naru.tech.common.exception;

import com.naru.tech.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    /**
     * 열거형 에러코드를 통해, 간단한 에러를 생성합니다.
     * @param errorCode 에러코드
     */
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 열거형 에러코드와 문자열 메세지를 통해, 자세한 에러를 생성합니다.
     * @param errorCode 에러코드
     * @param detailMessage 자세한 에러 메세지
     */
    public CustomException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

}
