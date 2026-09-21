package com.soupulsar.application.session;

import com.soupulsar.application.dto.request.ScheduleSessionRequest;
import com.soupulsar.application.dto.response.ScheduleSessionResponse;
import com.soupulsar.application.session.ScheduleSessionUseCase;
import com.soupulsar.application.session.shared.SessionAvailabilityChecker;
import com.soupulsar.domain.exceptions.AvailabilityNotFoundException;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScheduleSessionUseCaseTest {

    private SessionRepository sessionRepository;
    private SessionAvailabilityChecker sessionAvailabilityChecker;
    private ScheduleSessionUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepository.class);
        sessionAvailabilityChecker = mock(SessionAvailabilityChecker.class);
        useCase = new ScheduleSessionUseCase(sessionRepository, sessionAvailabilityChecker);
    }

    @Test
    void shouldScheduleSessionWhenAvailabilityIsValid() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2024, 6, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 10, 11, 0);

        ScheduleSessionRequest request = new ScheduleSessionRequest(clientId, specialistId, start, end);
        Session savedSession = Session.scheduleSession(specialistId, clientId, start, end);

        when(sessionRepository.save(any(Session.class))).thenReturn(savedSession);

        ScheduleSessionResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(savedSession.getSessionId(), response.sessionId());
        verify(sessionAvailabilityChecker).validate(specialistId, start, end, null);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void shouldThrowWhenAvailabilityValidationFails() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2024, 6, 10, 20, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 10, 21, 0);

        ScheduleSessionRequest request = new ScheduleSessionRequest(clientId, specialistId, start, end);

        doThrow(new AvailabilityNotFoundException())
                .when(sessionAvailabilityChecker)
                .validate(specialistId, start, end, null);

        assertThrows(AvailabilityNotFoundException.class, () -> useCase.execute(request));
    }
}
