package com.soupulsar.application.specialist.availability;

import com.soupulsar.domain.exceptions.AvailabilityNotFoundException;
import com.soupulsar.domain.exceptions.FutureSessionConflictException;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
import com.soupulsar.domain.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteAvailabilityUseCase {

    private final AvailabilityRepository availabilityRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public void execute(UUID id) {

        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(AvailabilityNotFoundException::new);

        if(sessionRepository.countConflictingFutureSessionsForAvailability(availability.getSpecialistId(),
                availability.getDayOfWeek(), availability.getStartTime(), availability.getEndTime()) > 0) {
            throw new FutureSessionConflictException("Cannot delete availability with conflicting future sessions");
        }
        availabilityRepository.deleteById(id);
    }
}