package com.soupulsar.application.session.shared;

import com.soupulsar.domain.exceptions.AvailabilityBlockException;
import com.soupulsar.domain.exceptions.AvailabilityNotFoundException;
import com.soupulsar.domain.exceptions.FutureSessionConflictException;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityBlockRepository;
import com.soupulsar.domain.repository.AvailabilityRepository;
import com.soupulsar.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SessionAvailabilityChecker {

    private final SessionRepository sessionRepository;
    private final AvailabilityRepository availabilityRepository;
    private final AvailabilityBlockRepository availabilityBlockRepository;

    public void validate(UUID specialistId, LocalDateTime startAt, LocalDateTime endAt, UUID sessionIdToExclude) {
        validateAvailability(specialistId, startAt, endAt);
        validateBlocks(specialistId, startAt, endAt);
        validateOverlappingSessions(specialistId, startAt, endAt, sessionIdToExclude);
    }

    private void validateAvailability(UUID specialistId, LocalDateTime startAt, LocalDateTime endAt) {
        List<Availability> availabilities = availabilityRepository.findBySpecialistIdAndDayOfWeek(specialistId, startAt.getDayOfWeek());

        boolean isAvailable = availabilities.stream().anyMatch(availability ->
                !startAt.toLocalTime().isBefore(availability.getStartTime()) &&
                        !endAt.toLocalTime().isAfter(availability.getEndTime())
        );

        if (!isAvailable) {
            throw new AvailabilityNotFoundException();
        }
    }

    private void validateBlocks(UUID specialistId, LocalDateTime startAt, LocalDateTime endAt) {
        boolean hasBlock = availabilityBlockRepository.existsOverlappingBlock(specialistId, startAt, endAt);

        if (hasBlock) {
            throw new AvailabilityBlockException("The requested time slot is blocked for the specialist.");
        }
    }

    private void validateOverlappingSessions(UUID specialistId, LocalDateTime startAt, LocalDateTime endAt, UUID sessionIdToExclude) {

        boolean hasOverlap = sessionRepository.existsOverlappingSessions(specialistId, startAt, endAt, sessionIdToExclude);

        if (hasOverlap) {
            throw new FutureSessionConflictException("The requested time slot overlaps with another session.");
        }

    }
}