package com.soupulsar.application.usecase.session;

import com.soupulsar.application.dto.request.ScheduleSessionRequest;
import com.soupulsar.application.dto.response.ScheduleSessionResponse;
import com.soupulsar.domain.event.SessionScheduledEvent;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.AvailabilityRepository;
import com.soupulsar.domain.repository.SessionRepository;
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
                sessionRepository.findOverlappingSessions(
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