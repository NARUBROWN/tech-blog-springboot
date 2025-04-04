package com.scout.tech.common.exception;


import com.scout.tech.common.enums.ErrorCode;

public class PostNotFoundException extends CustomException {

    /**
     * 포스트를 아이디로 찾을 수 없을 때 발생하는 에러
     * @param postId 포스트 ID
     */
    public PostNotFoundException(Long postId) {
        super(ErrorCode.POST_NOT_FOUND, "포스트를 찾을 수 없습니다  id :" + postId);
    }

    /**
     * 포스트를 슬러그로 찾을 수 없을 때 발생하는 에러
     * @param slug 포스트 슬러그
     */
    public PostNotFoundException(String slug) {
        super(ErrorCode.POST_NOT_FOUND, "포스트를 찾을 수 없습니다  slug :" + slug);
    }
}
