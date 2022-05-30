package ru.itis.exception;

public class UserNotFoundException extends RadioNotFoundException {

    public UserNotFoundException() {
        super("UserEntity not found");
    }
}
