package ru.itis.exception;

public class ChatNotFoundException extends RadioNotFoundException {
    public ChatNotFoundException() {
        super("ChatEntity not found");
    }
}
