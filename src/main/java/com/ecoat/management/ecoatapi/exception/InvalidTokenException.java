package com.ecoat.management.ecoatapi.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public InvalidTokenException(String exMessage) {
        super(exMessage);
    }
}