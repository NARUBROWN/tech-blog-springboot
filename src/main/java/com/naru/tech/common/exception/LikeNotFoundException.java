package com.naru.tech.common.exception;


import com.naru.tech.common.enums.ErrorCode;

public class LikeNotFoundException extends CustomException {

    public LikeNotFoundException() {
        super(ErrorCode.LIKE_NOT_FOUND, "좋아요를 찾을 수 없습니다");
    }
}
