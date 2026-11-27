package com.soupulsar.domain.exceptions;

public class ReviewModerationUnavailableException extends RuntimeException {
    public ReviewModerationUnavailableException(String message) {
        super(message);
    }

    public ReviewModerationUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
