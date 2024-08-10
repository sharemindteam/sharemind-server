package com.example.sharemind.wishList.exception;

import lombok.Getter;

@Getter
public class WishListException extends RuntimeException {

    private final WishListErrorCode errorCode;

    public WishListException(WishListErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
