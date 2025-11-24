package com.soupulsar.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {

    CLIENT,
    SPECIALIST,
    ADMIN;

    @JsonCreator
    public static UserRole fromString(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown UserRole: " + value);
    }
}