package com.giancarlos.exception;

public class OrderNotFoundException extends RuntimeException{
    private static final long serialVerisionUID = 4;

    public OrderNotFoundException(String message) {
        super(message);
    }
}
