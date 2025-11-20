package com.soupulsar.application.exceptions;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
