package com.soupulsar.infrastructure.persistence.mapper.client;

import com.soupulsar.domain.model.vo.EmergencyContact;
import com.soupulsar.infrastructure.persistence.entity.client.EmergencyContactEmbeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmergencyContactMapper {

    public static EmergencyContactEmbeddable toEmbeddable(EmergencyContact emergencyContact) {
        if (emergencyContact == null) return null;

        return EmergencyContactEmbeddable.builder()
                .name(emergencyContact.getName())
                .phoneNumber(emergencyContact.getPhoneNumber())
                .relationshipDegree(emergencyContact.getRelationshipDegree())
                .build();
    }

    public static EmergencyContact toValueObject(EmergencyContactEmbeddable emergencyContact) {
        if (emergencyContact == null) return null;

        return EmergencyContact.builder()
                .name(emergencyContact.getName())
                .phoneNumber(emergencyContact.getPhoneNumber())
                .relationshipDegree(emergencyContact.getRelationshipDegree())
                .build();
    }
}