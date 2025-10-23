package com.soupulsar.infrastructure.persistence.entity.client;

import com.soupulsar.domain.model.enums.RelationshipDegree;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public final class EmergencyContactEmbeddable {

    private String name;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private RelationshipDegree relationshipDegree;

}