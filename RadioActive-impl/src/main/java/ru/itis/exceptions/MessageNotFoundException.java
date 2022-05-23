package ru.itis.exceptions;

public class MessageNotFoundException extends RadioNotFoundException {
    public MessageNotFoundException() {
        super("Message not found");
    }
}
