package com.soupulsar.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("User not found with ID: " + userId);
    }
    public  UserNotFoundException() {
        super("User not found");
    }

}