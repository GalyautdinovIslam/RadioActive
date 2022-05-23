package ru.itis.exceptions;

public class ChatNotFoundException extends RadioNotFoundException {
    public ChatNotFoundException() {
        super("Chat not found");
    }
}
