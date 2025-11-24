package com.soupulsar.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RelationshipDegree {

    CONJUGE,
    FILHO,
    IRMAO,
    MAE,
    NETO,
    PAI,
    SOBRINHO,
    TIO,
    AMIGO,
    CUNHADO,
    GENRO,
    NORA,
    SOGRO,
    AVO,
    COMPANHEIRO,
    OUTRO;

    @JsonCreator
    public static RelationshipDegree fromString(String value) {
        for (RelationshipDegree degree : RelationshipDegree.values()) {
            if (degree.name().equalsIgnoreCase(value)) {
                return degree;
            }
        }
        throw new IllegalArgumentException("Unknown RelationshipDegree: " + value);
    }
}