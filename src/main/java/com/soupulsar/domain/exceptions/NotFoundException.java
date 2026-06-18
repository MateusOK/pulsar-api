package com.soupulsar.domain.exceptions;

public abstract class NotFoundException extends RuntimeException {
    protected NotFoundException(String message) {
        super(message);
    }
    protected NotFoundException(String message, Throwable cause) {}

    protected NotFoundException() {}
}