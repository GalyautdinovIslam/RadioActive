package ru.itis.exceptions;

public class RoomNotFoundException extends RadioNotFoundException {
    public RoomNotFoundException() {
        super("Room not found");
    }
}
