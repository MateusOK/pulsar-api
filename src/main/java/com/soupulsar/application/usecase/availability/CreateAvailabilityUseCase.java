package com.soupulsar.application.usecase.availability;

import com.soupulsar.application.dto.request.CreateAvailabilityRequest;
import com.soupulsar.application.dto.response.CreateAvailabilityResponse;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
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
