package com.soupulsar.modulith.scheduling.application.usecase;

import com.soupulsar.modulith.scheduling.application.dto.CreateAvailabilityRequest;
import com.soupulsar.modulith.scheduling.application.dto.CreateAvailabilityResponse;
import com.soupulsar.modulith.scheduling.domain.model.Availability;
import com.soupulsar.modulith.scheduling.domain.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateAvailabilityUseCase {

    private final AvailabilityRepository repository;

    public CreateAvailabilityResponse execute(CreateAvailabilityRequest request) {
        if (request.endTime().isBefore(request.startTime()) ||
        request.endTime().equals(request.startTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        Availability availability = new Availability(
                UUID.randomUUID(),
                request.specialistId(),
                request.dayOfWeek(),
                request.startTime(),
                request.endTime()
        );

        return new CreateAvailabilityResponse(repository.save(availability));
    }

}
