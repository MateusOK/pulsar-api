package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.enums.SpecialistType;
import lombok.Builder;

import java.util.List;

@Builder
public record SpecialistProfileResponse(
        String name,
        String email,
        String telephone,
        SpecialistType specialistType,
        List<String> therapeuticApproaches,
        List<String> clinicalSpecialties,
        List<String> education,
        AddressResponse addressResponse
) implements UserProfileResponse {
}
