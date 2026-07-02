package com.nhnacademy.data4library.exception;

import lombok.Getter;

@Getter
public class NaruApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public NaruApiException(ErrorCode errorCode, String error) {
        super(error);
        this.errorCode = errorCode;
    }
}