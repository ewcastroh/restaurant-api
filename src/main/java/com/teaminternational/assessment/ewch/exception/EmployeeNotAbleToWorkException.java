package com.teaminternational.assessment.ewch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmployeeNotAbleToWorkException extends RuntimeException {

    public EmployeeNotAbleToWorkException(String message) {
        super(message);
    }

    public EmployeeNotAbleToWorkException(String message, Throwable cause) {
        super(message, cause);
    }
}
