package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.enums.SpecialistType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record SpecialistDetailsResponse(
        UUID id,
        String name,
        String registrationNumber,
        String city,
        String state,
        String about,
        SpecialistType specialistType,
        String introductionVideoUrl,
        String base64Image,
        Long sessionsCompleted,
        BigDecimal price,
        String personalDescription,
        List<String> specialties,
        List<String> approaches,
        List<String> formations
) {
}
