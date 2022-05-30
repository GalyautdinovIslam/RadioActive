package ru.itis.exception;

import org.springframework.http.HttpStatus;

public class RadioNotFoundException extends RadioServiceException {

    public RadioNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
