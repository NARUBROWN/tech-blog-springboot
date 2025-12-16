package com.naru.tech.common.exception;


import com.naru.tech.common.enums.ErrorCode;

public class ImageMetadataNotFoundException extends CustomException {

    public ImageMetadataNotFoundException() {
        super(ErrorCode.IMAGE_NOT_FOUND, "이미지 메타데이터를 찾을 수 없습니다.");
    }
}
