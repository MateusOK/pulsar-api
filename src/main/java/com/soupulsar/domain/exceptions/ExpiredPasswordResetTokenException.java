package com.soupulsar.domain.exceptions;

public class ExpiredPasswordResetTokenException extends BusinessRuleException {
    public ExpiredPasswordResetTokenException() {
        super("Token has expired or has already been used");
    }
}
