package com.soupulsar.application.session;

import com.soupulsar.application.dto.request.RescheduleSessionRequest;
import com.soupulsar.application.dto.response.ScheduleSessionResponse;
import com.soupulsar.application.session.RescheduleSessionUseCase;
import com.soupulsar.application.session.shared.SessionAvailabilityChecker;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.SessionNotFoundException;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RescheduleSessionUseCaseTest {

    private SecurityUtils securityUtils;
    private SessionRepository sessionRepository;
    private SessionAvailabilityChecker sessionAvailabilityChecker;
    private RescheduleSessionUseCase useCase;

    @BeforeEach
    void setUp() {
        securityUtils = mock(SecurityUtils.class);
        sessionRepository = mock(SessionRepository.class);
        sessionAvailabilityChecker = mock(SessionAvailabilityChecker.class);
        useCase = new RescheduleSessionUseCase(securityUtils, sessionRepository, sessionAvailabilityChecker);
    }

    @Test
    void shouldRescheduleSessionWhenUserIsAuthorizedAndSlotIsAvailable() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        LocalDateTime originalStart = LocalDateTime.of(2024, 6, 10, 10, 0);
        LocalDateTime originalEnd = LocalDateTime.of(2024, 6, 10, 11, 0);
        LocalDateTime newStart = LocalDateTime.of(2024, 6, 10, 12, 0);
        LocalDateTime newEnd = LocalDateTime.of(2024, 6, 10, 13, 0);

        Session session = Session.restore(sessionId, specialistId, clientId, originalStart, originalEnd, SessionStatus.CONFIRMED);
        RescheduleSessionRequest request = new RescheduleSessionRequest(sessionId, newStart, newEnd);

        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        ScheduleSessionResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(sessionId, response.sessionId());
        assertEquals(newStart, session.getStartAt());
        assertEquals(newEnd, session.getEndAt());
        verify(sessionAvailabilityChecker).validate(specialistId, newStart, newEnd, sessionId);
        verify(sessionRepository).save(session);
    }

    @Test
    void shouldThrowWhenUserIsNotAuthorizedToReschedule() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();

        Session session = Session.restore(sessionId, specialistId, clientId,
                LocalDateTime.of(2024, 6, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 11, 0),
                SessionStatus.CONFIRMED);

        when(securityUtils.getCurrentUserId()).thenReturn(anotherUserId);
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));

        RescheduleSessionRequest request = new RescheduleSessionRequest(sessionId,
                LocalDateTime.of(2024, 6, 10, 12, 0),
                LocalDateTime.of(2024, 6, 10, 13, 0));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        verify(sessionAvailabilityChecker, never()).validate(any(), any(), any(), any());
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenSessionDoesNotExist() {
        UUID sessionId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());

        RescheduleSessionRequest request = new RescheduleSessionRequest(sessionId,
                LocalDateTime.of(2024, 6, 10, 12, 0),
                LocalDateTime.of(2024, 6, 10, 13, 0));

        assertThrows(SessionNotFoundException.class, () -> useCase.execute(request));
    }

    @Test
    void shouldPropagateAvailabilityValidationFailure() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        Session session = Session.restore(sessionId, specialistId, clientId,
                LocalDateTime.of(2024, 6, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 11, 0),
                SessionStatus.CONFIRMED);

        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));
        doThrow(new IllegalArgumentException("The requested time slot overlaps with another session."))
                .when(sessionAvailabilityChecker)
                .validate(specialistId,
                        LocalDateTime.of(2024, 6, 10, 12, 0),
                        LocalDateTime.of(2024, 6, 10, 13, 0),
                        sessionId);

        RescheduleSessionRequest request = new RescheduleSessionRequest(sessionId,
                LocalDateTime.of(2024, 6, 10, 12, 0),
                LocalDateTime.of(2024, 6, 10, 13, 0));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        assertEquals("The requested time slot overlaps with another session.", ex.getMessage());
    }
}
