package com.soupulsar.domain.exceptions;

public class FutureSessionConflictException extends BusinessRuleException {
    public FutureSessionConflictException(String message) {
        super(message);
    }
}
