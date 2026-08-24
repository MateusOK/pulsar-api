package com.soupulsar.domain.exceptions;

public class AvailabilityBlockNotFoundException extends RuntimeException {
    public AvailabilityBlockNotFoundException() {
        super("Availability block not found");
    }
}
