package com.ecoat.management.ecoatapi.exception;
public class EcoatsException extends RuntimeException {
    public EcoatsException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public EcoatsException(String exMessage) {
        super(exMessage);
    }
}