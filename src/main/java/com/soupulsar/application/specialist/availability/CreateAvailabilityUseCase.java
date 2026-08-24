package com.soupulsar.application.specialist.availability;

import com.soupulsar.domain.exceptions.AvailabilityOverlapException;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CreateAvailabilityUseCase {

    private final AvailabilityRepository repository;

    @Transactional
    public CreateAvailabilityResponse execute(CreateAvailabilityRequest request) {

        Availability availability = Availability.create(
                request.specialistId(),
                request.dayOfWeek(),
                request.startTime(),
                request.endTime()
        );

        List<Availability> existingAvailabilities = repository.findBySpecialistIdAndDayOfWeek(
                request.specialistId(),
                request.dayOfWeek()
        );

        if (existingAvailabilities.stream().anyMatch(availability::overlaps)) {
            throw new AvailabilityOverlapException();
        }

        return new CreateAvailabilityResponse(repository.save(availability));
    }
}