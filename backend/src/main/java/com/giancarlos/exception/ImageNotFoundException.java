package com.giancarlos.exception;

public class ImageNotFoundException extends RuntimeException{
    private static final long serialVerisionUID = 3;

    public ImageNotFoundException(String message) {
        super(message);
    }
}
