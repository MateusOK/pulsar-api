package com.soupulsar.application.specialist.availability;

import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetSpecialistAvailabilitiesUseCase {

    private final AvailabilityRepository repository;
    private final SecurityUtils securityUtils;

    public GetSpecialistAvailabilitiesResponse execute() {

        UUID specialistId = securityUtils.getCurrentUserId();

        List<Availability> availabilities = repository.findBySpecialistId(specialistId);

        return new  GetSpecialistAvailabilitiesResponse(availabilities);
    }
}
