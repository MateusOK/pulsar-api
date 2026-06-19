package com.soupulsar.domain.exceptions;

public abstract class BusinessRuleException extends RuntimeException {
    protected BusinessRuleException(String message) {
        super(message);
    }
    protected BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
    protected BusinessRuleException(){}
}