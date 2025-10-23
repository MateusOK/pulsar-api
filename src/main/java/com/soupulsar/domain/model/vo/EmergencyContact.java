package com.soupulsar.domain.model.vo;

import com.soupulsar.domain.model.enums.RelationshipDegree;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmergencyContact {

    String name;
    String phoneNumber;
    RelationshipDegree relationshipDegree;

    public EmergencyContact withPhoneNumber(String newPhoneNumber) {
        return EmergencyContact.builder()
                .name(this.name)
                .phoneNumber(newPhoneNumber)
                .relationshipDegree(this.relationshipDegree)
                .build();
    }
}