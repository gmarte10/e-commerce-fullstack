package com.giancarlos.exception;

public class OrderItemNotFoundException extends RuntimeException{
    private static final long serialVerisionUID = 5;
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
