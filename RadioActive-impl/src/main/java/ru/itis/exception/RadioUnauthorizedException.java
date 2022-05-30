package ru.itis.exception;

import org.springframework.http.HttpStatus;

public class RadioUnauthorizedException extends RadioServiceException {
    public RadioUnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
