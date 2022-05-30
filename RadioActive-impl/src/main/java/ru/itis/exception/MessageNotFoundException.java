package ru.itis.exception;

public class MessageNotFoundException extends RadioNotFoundException {
    public MessageNotFoundException() {
        super("MessageEntity not found");
    }
}
