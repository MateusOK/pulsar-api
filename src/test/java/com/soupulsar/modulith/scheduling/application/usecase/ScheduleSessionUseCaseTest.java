package com.soupulsar.modulith.scheduling.application.usecase;

import com.soupulsar.modulith.scheduling.application.dto.ScheduleSessionRequest;
import com.soupulsar.modulith.scheduling.application.dto.ScheduleSessionResponse;
import com.soupulsar.modulith.scheduling.domain.events.SessionScheduledEvent;
import com.soupulsar.modulith.scheduling.domain.model.Availability;
import com.soupulsar.modulith.scheduling.domain.model.Session;
import com.soupulsar.modulith.scheduling.domain.repository.AvailabilityRepository;
import com.soupulsar.modulith.scheduling.domain.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleSessionUseCaseTest {

    private SessionRepository sessionRepository;
    private AvailabilityRepository availabilityRepository;
    private ApplicationEventPublisher eventPublisher;
    private ScheduleSessionUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepository.class);
        availabilityRepository = mock(AvailabilityRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        useCase = new ScheduleSessionUseCase(sessionRepository, availabilityRepository, eventPublisher);
    }

    @Test
    void shouldScheduleSessionWhenAvailabilityIsValidAndNoOverlap() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2024, 6, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 10, 11, 0);

        ScheduleSessionRequest request = new ScheduleSessionRequest(clientId, specialistId, start, end);

        Availability availability = new Availability(UUID.randomUUID(), specialistId, start.getDayOfWeek(), LocalTime.of(9, 0), LocalTime.of(17, 0));
        when(availabilityRepository.findBySpecialistIdAndDayOfWeek(any(), any())).thenReturn(List.of(availability));
        when(sessionRepository.findBySpecialistIdAndTimeRange(any(), any(), any())).thenReturn(List.of());

        Session session = Session.scheduleSession(specialistId, clientId, start, end);
        when(sessionRepository.save(any())).thenReturn(session);

        ScheduleSessionResponse response = useCase.execute(request);

        assertEquals(session.getSessionId(), response.sessionId());
        verify(eventPublisher).publishEvent(any(SessionScheduledEvent.class));
    }

    @Test
    void shouldThrowWhenNoMatchingAvailability() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2024, 6, 10, 20, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 10, 21, 0);

        ScheduleSessionRequest request = new ScheduleSessionRequest(clientId, specialistId, start, end);

        Availability availability = new Availability(UUID.randomUUID(), specialistId, start.getDayOfWeek(), LocalTime.of(9, 0), LocalTime.of(17, 0));
        when(availabilityRepository.findBySpecialistIdAndDayOfWeek(any(), any())).thenReturn(List.of(availability));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        assertEquals("Specialist is not available at the requested time", ex.getMessage());
    }

    @Test
    void shouldThrowWhenSessionOverlaps() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2024, 6, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 10, 11, 0);

        ScheduleSessionRequest request = new ScheduleSessionRequest(clientId, specialistId, start, end);

        Availability availability = new Availability(UUID.randomUUID(), specialistId, start.getDayOfWeek(), LocalTime.of(9, 0), LocalTime.of(17, 0));
        when(availabilityRepository.findBySpecialistIdAndDayOfWeek(any(), any())).thenReturn(List.of(availability));

        Session overlappingSession = Session.scheduleSession(specialistId, UUID.randomUUID(), start.minusMinutes(30), end.plusMinutes(30));
        when(sessionRepository.findBySpecialistIdAndTimeRange(any(), any(), any())).thenReturn(List.of(overlappingSession));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        assertEquals("The requested time overlaps with an existing session", ex.getMessage());
    }
}
