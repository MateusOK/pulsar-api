package com.soupulsar.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SpecialistType {

    PSICOLOGO,
    PSIQUIATRA,
    TERAPEUTA,
    EDUCADOR_FISICO,
    NUTRICIONISTA,
    ASSESSOR_FINANCEIRO;

    @JsonCreator
    public static SpecialistType fromString(String value) {
        for (SpecialistType type : SpecialistType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown SpecialistType: " + value);
    }
}