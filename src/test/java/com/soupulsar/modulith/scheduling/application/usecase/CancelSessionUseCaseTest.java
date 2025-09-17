package com.soupulsar.modulith.scheduling.application.usecase;

import com.soupulsar.modulith.scheduling.application.dto.SessionResponse;
import com.soupulsar.modulith.scheduling.domain.model.Session;
import com.soupulsar.modulith.scheduling.domain.repository.SessionRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelSessionUseCaseTest {

    @Test
    void execute_shouldCancelSessionSuccessfully() {
        SessionRepository repo = mock(SessionRepository.class);
        Session session = mock(Session.class);
        UUID sessionId = UUID.randomUUID();

        when(repo.findBySessionId(sessionId)).thenReturn(Optional.of(session));
        when(repo.save(session)).thenReturn(session);

        CancelSessionUseCase useCase = new CancelSessionUseCase(repo);
        SessionResponse response = useCase.execute(sessionId);

        verify(session).cancelSession();
        verify(repo).save(session);
        assertNotNull(response);
    }

    @Test
    void execute_shouldThrowWhenSessionNotFound() {
        SessionRepository repo = mock(SessionRepository.class);
        UUID sessionId = UUID.randomUUID();
        when(repo.findBySessionId(sessionId)).thenReturn(Optional.empty());

        CancelSessionUseCase useCase = new CancelSessionUseCase(repo);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(sessionId));
    }
}
