package com.soupulsar.application.specialist.availability;

import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.FutureSessionConflictException;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
import com.soupulsar.domain.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UpdateAvailabilityDayUseCase {

    private final AvailabilityRepository repository;
    private final SessionRepository sessionRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public void execute(UpdateAvailabilityDayRequest request) {

        UUID specialistId = securityUtils.getCurrentUserId();
        List<Availability> availabilities = repository.findBySpecialistIdAndDayOfWeek(specialistId, request.dayOfWeek());

        if (!request.enabled()) {
            validateNoFutureSessions(specialistId, availabilities);
        }

        availabilities.forEach(availability -> {
            if (request.enabled()) {
                availability.enable();
            } else {
                availability.disable();
            }
        });

        repository.saveAll(availabilities);

    }

    private void validateNoFutureSessions(UUID specialistId, List<Availability> availabilities) {
        for (Availability availability : availabilities) {
            long conflicts = sessionRepository.countConflictingFutureSessionsForAvailability(
                    specialistId,
                    availability.getDayOfWeek(),
                    availability.getStartTime(),
                    availability.getEndTime()
            );
            if (conflicts > 0) {
                throw new FutureSessionConflictException("Cannot update availability with conflicting future sessions");
            }
        }
    }
}