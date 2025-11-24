package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.enums.SpecialistType;

import java.math.BigDecimal;
import java.util.List;

public record GetSpecialistById(
        String name,
        String registrationNumber,
        String city,
        String state,
        String about,
        SpecialistType specialistType,
        String base64Image,
        Long sessionsCompleted,
        BigDecimal price,
        String presentationVideoUrl,
        String personalDescription,
        List<String> approaches,
        List<String> specialties,
        List<String> formations

) {
}
