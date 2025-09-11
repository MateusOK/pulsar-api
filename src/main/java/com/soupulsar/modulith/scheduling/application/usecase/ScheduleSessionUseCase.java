package com.soupulsar.modulith.scheduling.application.usecase;

import com.soupulsar.modulith.scheduling.application.dto.ScheduleSessionRequest;
import com.soupulsar.modulith.scheduling.application.dto.ScheduleSessionResponse;
import com.soupulsar.modulith.scheduling.domain.events.SessionScheduledEvent;
import com.soupulsar.modulith.scheduling.domain.model.Availability;
import com.soupulsar.modulith.scheduling.domain.model.Session;
import com.soupulsar.modulith.scheduling.domain.repository.AvailabilityRepository;
import com.soupulsar.modulith.scheduling.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@RequiredArgsConstructor
public class ScheduleSessionUseCase {

    private final SessionRepository sessionRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ScheduleSessionResponse execute(ScheduleSessionRequest request) {

        List<Availability> availabilities =
                availabilityRepository.findBySpecialistIdAndDayOfWeek(request.specialistId(), request.startTime().getDayOfWeek());

        boolean isAvailable = availabilities.stream().anyMatch(availability ->
                !request.startTime().toLocalTime().isBefore(availability.getStartTime()) &&
                !request.endTime().toLocalTime().isAfter(availability.getEndTime())
        );

        if (!isAvailable) {
            throw new IllegalArgumentException("Specialist is not available at the requested time");
        }

        List<Session> existingSessions =
                sessionRepository.findBySpecialistIdAndTimeRange(
                        request.specialistId(),
                        request.startTime(),
                        request.endTime()
                );

        boolean hasOverlap = existingSessions.stream().anyMatch(session ->
                session.overlaps(request.startTime(), request.endTime())
        );

        if(hasOverlap){
            throw new IllegalArgumentException("The requested time overlaps with an existing session");
        }

        Session savedSession = sessionRepository.save(Session.scheduleSession(
                request.specialistId(),
                request.clientId(),
                request.startTime(),
                request.endTime()
        ));

        eventPublisher.publishEvent(new SessionScheduledEvent(savedSession));

        return new ScheduleSessionResponse(savedSession);
    }
}