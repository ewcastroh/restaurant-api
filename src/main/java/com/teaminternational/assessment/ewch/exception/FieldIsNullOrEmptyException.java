package com.teaminternational.assessment.ewch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FieldIsNullOrEmptyException extends RuntimeException {

    public FieldIsNullOrEmptyException(String message) {
        super(message);
    }

    public FieldIsNullOrEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
