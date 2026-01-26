package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.Presentation;
import com.soupulsar.domain.model.vo.RegistrationNumber;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record SpecialistDetailsResponse(
        UUID id,
        String name,
        RegistrationNumber registrationNumber,
        String city,
        String state,
        Presentation presentation,
        SpecialistType specialistType,
        Long sessionsCompleted,
        Money price
) {
}
