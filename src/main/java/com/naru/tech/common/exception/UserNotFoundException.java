package com.naru.tech.common.exception;

import com.naru.tech.common.enums.ErrorCode;

public class UserNotFoundException extends CustomException {

    /**
     * 유저를 찾을 수 없을때 발생하는 에러
     * @param userId 유저 ID
     */
    public UserNotFoundException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다. id: " + userId);
    }

    public UserNotFoundException(String username) {
        super(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다. username: " + username);
    }
}
