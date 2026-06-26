package com.nhnacademy.data4library.exception;

import lombok.Getter;

@Getter
public class NaruApiException extends RuntimeException {

    private final String errorCode;

    public NaruApiException(String errorCode, String error) {
        super(error);
        this.errorCode = "NARU_" + errorCode.toUpperCase();
    }
}