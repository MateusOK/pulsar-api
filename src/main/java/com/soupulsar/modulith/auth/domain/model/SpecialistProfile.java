package com.soupulsar.modulith.auth.domain.model;

import com.soupulsar.modulith.auth.domain.model.vo.Presentation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
@Builder
public class SpecialistProfile {

    private final UUID profileId;
    private final UUID userId;
    private final String registrationNumber;
    private Presentation presentation;
    @Singular("formation")
    private List<String> formations;
    @Singular("specialty")
    private List<String> specialties;
    @Singular("approach")
    private List<String> approaches;


    public static SpecialistProfile create(UUID userId, String registrationNumber, Presentation presentation, List<String> formation, List<String> specialties, List<String> approaches) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        return SpecialistProfile.builder()
                .profileId(UUID.randomUUID())
                .userId(userId)
                .registrationNumber(registrationNumber)
                .presentation(presentation)
                .formations(formation)
                .specialties(specialties)
                .approaches(approaches)
                .build();
    }

    public static SpecialistProfile restore(UUID profileId, UUID userId, String registrationNumber ,Presentation presentation, List<String> formation, List<String> specialties, List<String> approaches) {
        return SpecialistProfile.builder()
                .profileId(profileId)
                .userId(userId)
                .registrationNumber(registrationNumber)
                .presentation(presentation)
                .formations(formation)
                .specialties(specialties)
                .approaches(approaches)
                .build();
    }
}