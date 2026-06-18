package com.soupulsar.domain.exceptions;

public class InvalidPasswordResetTokenException extends BusinessRuleException {
    public InvalidPasswordResetTokenException() {
        super("Invalid password reset token");
    }
}
