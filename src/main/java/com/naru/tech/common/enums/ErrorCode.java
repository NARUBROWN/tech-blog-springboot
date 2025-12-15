package com.naru.tech.common.enums;

public enum ErrorCode {
    CATEGORY_NOT_FOUND(404, "카테고리를 찾을 수 없습니다."),
    LIKE_NOT_FOUND(404, "좋아요를 찾을 수 없습니다."),
    POST_NOT_FOUND(404, "포스트를 찾을 수 없습니다."),
    POST_TAG_NOT_FOUND(404, "포스트 태그를 찾을 수 없습니다."),
    TAG_NOT_FOUND(404, "태그를 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(403, "패스워드가 일치하지 않습니다."),
    INVALID_TOKEN(403, "토큰이 유효하지 않습니다.");

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
