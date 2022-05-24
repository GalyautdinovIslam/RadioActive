package ru.itis.exceptions;

public class UserNotFoundException extends RadioNotFoundException {

    public UserNotFoundException() {
        super("User not found");
    }
}
