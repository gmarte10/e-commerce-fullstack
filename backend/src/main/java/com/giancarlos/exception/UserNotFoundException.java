package com.giancarlos.exception;

public class UserNotFoundException extends RuntimeException{
    private static final long serialVerisionUID = 1;

    public UserNotFoundException(String message) {
        super(message);
    }
}
