package com.soupulsar.domain.model.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record RegistrationNumber(String value) {

    @JsonCreator
    public RegistrationNumber{
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Registration number cannot be null or blank");
        }
    }

    @JsonValue
    public String value() {
        return value;
    }

}