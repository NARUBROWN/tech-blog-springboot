package com.naru.tech.common.exception;

import com.naru.tech.common.enums.ErrorCode;

public class CategoryNotFoundException extends CustomException {

    /**
     * 카테고리를 찾을 수 없을때 발생하는 오류
     * @param categoryId 카테고리 ID
     */
    public CategoryNotFoundException(Long categoryId) {
        super(ErrorCode.CATEGORY_NOT_FOUND, "카테고리를 찾을 수 없습니다. id : " + categoryId);
    }
}
