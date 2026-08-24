package com.soupulsar.domain.exceptions;

public class AvailabilityNotFoundException extends NotFoundException {
    public AvailabilityNotFoundException() {
        super("Availability not found");
    }
}
