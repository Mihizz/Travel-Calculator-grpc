package com.fon.master.authentication_service.exception;

import org.springframework.http.HttpStatus;

public class AuthServiceException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public AuthServiceException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public AuthServiceException(String message, HttpStatus httpStatus, String body) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
