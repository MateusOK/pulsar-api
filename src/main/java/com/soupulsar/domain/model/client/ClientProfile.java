package com.soupulsar.domain.model.client;

import com.soupulsar.domain.model.vo.EmergencyContact;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class ClientProfile {

    private final UUID profileId;
    private final UUID userId;
    private final Date dateOfBirth;
    private final EmergencyContact emergencyContact;


    public static ClientProfile create(UUID userId, Date dateOfBirth, EmergencyContact emergencyContact) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        if (dateOfBirth == null) throw new IllegalArgumentException("Date of birth cannot be null");
        if (emergencyContact == null) throw new IllegalArgumentException("Emergency contact cannot be null");
        return ClientProfile.builder()
                .profileId(UUID.randomUUID())
                .userId(userId)
                .dateOfBirth(dateOfBirth)
                .emergencyContact(emergencyContact)
                .build();
    }

    public static ClientProfile restore(UUID profileId, UUID userId, Date dateOfBirth, EmergencyContact emergencyContact) {
        return ClientProfile.builder()
                .profileId(profileId)
                .userId(userId)
                .dateOfBirth(dateOfBirth)
                .emergencyContact(emergencyContact)
                .build();
    }
}