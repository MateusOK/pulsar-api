package com.soupulsar.application.specialist.availability;

import com.soupulsar.domain.exceptions.AvailabilityNotFoundException;
import com.soupulsar.domain.exceptions.AvailabilityOverlapException;
import com.soupulsar.domain.exceptions.FutureSessionConflictException;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
import com.soupulsar.domain.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UpdateAvailabilityUseCase {

    private final AvailabilityRepository repository;
    private final SessionRepository sessionRepository;

    @Transactional
    public void execute(UUID id, UpdateAvailabilityRequest request){

        Availability availability = repository.findById(id)
                .orElseThrow(AvailabilityNotFoundException::new);

        availability.update(
                request.startTime(),
                request.endTime()
        );

        List<Availability> existing = repository.findBySpecialistIdAndDayOfWeek(availability.getSpecialistId(), availability.getDayOfWeek());

        if(existing.stream().filter(a -> !a.getId().equals(availability.getId())).anyMatch(availability::overlaps)){
            throw new AvailabilityOverlapException();
        }

        if(sessionRepository.countConflictingFutureSessionsForAvailability(availability.getSpecialistId(),
                availability.getDayOfWeek(), availability.getStartTime(), availability.getEndTime()) > 0) {
            throw new FutureSessionConflictException("Cannot update availability with conflicting future sessions");
        }

        repository.save(availability);
    }
}