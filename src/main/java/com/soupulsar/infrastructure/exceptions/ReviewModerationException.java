package com.soupulsar.infrastructure.exceptions;

public class ReviewModerationException extends RuntimeException {
    public ReviewModerationException(String message) {
        super(message);
    }

    public ReviewModerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
