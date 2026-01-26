package com.soupulsar.domain.model.vo;

public record RegistrationNumber(String value) {
    public RegistrationNumber{
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Registration number cannot be null or blank");
        }
    }
}