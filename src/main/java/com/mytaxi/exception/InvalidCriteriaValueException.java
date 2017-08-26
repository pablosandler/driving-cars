package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid criteria value")
public class InvalidCriteriaValueException extends Exception {

    private static final long serialVersionUID = -284028889660906887L;

    public InvalidCriteriaValueException(String message) {
        super(message);
    }
}
