package com.soupulsar.infrastructure.exceptions;

public class FailedAsaasCustomerException extends RuntimeException {
    public FailedAsaasCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}