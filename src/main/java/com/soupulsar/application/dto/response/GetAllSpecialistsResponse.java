package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.enums.SpecialistType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record GetAllSpecialistsResponse(
        UUID id,
        String name,
        String city,
        String state,
        List<String> specialties,
        List<String> approaches,
        String base64Image,
        SpecialistType specialistType,
        BigDecimal price,
        Long sessionsCompleted
) {
}