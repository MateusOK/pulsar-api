package com.soupulsar.domain.model.specialist;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.Presentation;
import com.soupulsar.domain.model.vo.RegistrationNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SpecialistProfile {

    private final UUID profileId;
    private final UUID userId;
    private final RegistrationNumber registrationNumber;
    private final SpecialistType specialistType;
    private Money sessionPrice;
    private Presentation presentation;


    public static SpecialistProfile create(UUID userId, RegistrationNumber registrationNumber, Presentation presentation,
                                           SpecialistType specialistType, Money sessionPrice) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        return SpecialistProfile.builder()
                .profileId(UUID.randomUUID())
                .userId(userId)
                .registrationNumber(registrationNumber)
                .presentation(presentation)
                .specialistType(specialistType)
                .sessionPrice(sessionPrice)
                .build();
    }

    public static SpecialistProfile restore(UUID profileId, UUID userId, RegistrationNumber registrationNumber ,Presentation presentation) {
        return SpecialistProfile.builder()
                .profileId(profileId)
                .userId(userId)
                .registrationNumber(registrationNumber)
                .presentation(presentation)
                .build();
    }
}