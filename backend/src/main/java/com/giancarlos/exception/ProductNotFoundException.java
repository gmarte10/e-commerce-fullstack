package com.giancarlos.exception;

public class ProductNotFoundException extends RuntimeException{
    private static final long serialVerisionUID = 2;

    public ProductNotFoundException(String message) {
        super(message);
    }
}
