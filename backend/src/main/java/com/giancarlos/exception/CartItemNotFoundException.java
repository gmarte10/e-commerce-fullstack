package com.giancarlos.exception;

public class CartItemNotFoundException extends RuntimeException{
    private static final long serialVerisionUID = 4;

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
