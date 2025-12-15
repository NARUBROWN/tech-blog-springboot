package com.naru.tech.common.exception;

import com.naru.tech.common.enums.ErrorCode;

public class LoginException extends CustomException {

    /**
     * 로그인에 문제가 생겼을때 발생하는 에러
     * @param errorCode 에러코드
     */
    public LoginException(ErrorCode errorCode) {
        super(errorCode, "로그인에 문제가 있습니다.");
    }
}
