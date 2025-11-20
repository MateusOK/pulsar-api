package com.soupulsar.infrastructure.persistence.mapper.client;

import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.infrastructure.persistence.entity.client.ClientProfileEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientProfileMapper {

    public static ClientProfileEntity toEntity(ClientProfile clientProfile) {
        if (clientProfile == null) return null;

        ClientProfileEntity entity = new ClientProfileEntity();
        entity.setUserId(clientProfile.getUserId());
        entity.setDateOfBirth(clientProfile.getDateOfBirth());
        entity.setEmergencyContact(EmergencyContactMapper.toEmbeddable(clientProfile.getEmergencyContact()));
        return entity;
    }

    public static ClientProfile toModel(ClientProfileEntity entity) {
        if (entity == null) return null;

        return ClientProfile.builder()
                .userId(entity.getUserId())
                .dateOfBirth(entity.getDateOfBirth())
                .emergencyContact(EmergencyContactMapper.toValueObject(entity.getEmergencyContact()))
                .build();


    }

}
