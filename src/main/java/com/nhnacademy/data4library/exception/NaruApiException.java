package com.nhnacademy.data4library.exception;

public class NaruApiException extends RuntimeException {
    public NaruApiException(String message) {
        super(message);
    }

    public NaruApiException(String message, Throwable cause) {
        super(message, cause);
    }
}