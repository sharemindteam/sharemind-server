package com.example.sharemind.wishList.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum WishListErrorCode {

    WISH_LIST_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 찜하기에 등록된 상담사입니다."),
    WISH_LIST_NOT_EXIST(HttpStatus.BAD_REQUEST, "기존 찜하기에 등록되어있지 않은 상담사입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    WishListErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
