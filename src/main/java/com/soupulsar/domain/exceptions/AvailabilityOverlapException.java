package com.soupulsar.domain.exceptions;

public class AvailabilityOverlapException extends BusinessRuleException {
        public AvailabilityOverlapException() {
        super("Availability overlaps with existing availability.");
    }
}