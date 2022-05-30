package ru.itis.exceptions;

import java.io.IOException;

public class AudioNotFoundException extends RadioNotFoundException {

    public AudioNotFoundException() {
        super("Audio file does not exist");
    }

    public AudioNotFoundException(IOException e) {
        super(e.getMessage());
    }
}
