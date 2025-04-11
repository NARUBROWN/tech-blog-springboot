package com.scout.tech.common.exception;

import com.scout.tech.common.enums.ErrorCode;

public class TokenInvalidException extends CustomException {

    /**
     * 토큰이 유효하지 않을때 발생하는 에러
     */
    public TokenInvalidException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
