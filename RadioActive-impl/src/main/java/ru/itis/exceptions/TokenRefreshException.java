package ru.itis.exceptions;

import org.springframework.http.HttpStatus;

public class TokenRefreshException extends RadioServiceException {
    public TokenRefreshException(String token, String message) {
        super(HttpStatus.UNAUTHORIZED, String.format("Failed for [%s]: %s", token, message));
    }
}
