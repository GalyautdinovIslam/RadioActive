package ru.itis.exception;

public class RoomNotFoundException extends RadioNotFoundException {
    public RoomNotFoundException() {
        super("RoomEntity not found");
    }
}
