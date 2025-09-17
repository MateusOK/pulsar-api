package com.soupulsar.modulith.scheduling.application.usecase;

import com.soupulsar.modulith.scheduling.application.dto.SessionResponse;
import com.soupulsar.modulith.scheduling.domain.model.Session;
import com.soupulsar.modulith.scheduling.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CancelSessionUseCase {

    private final SessionRepository sessionRepository;

    public SessionResponse execute(UUID sessionId)  {
        Session session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        session.cancelSession();
        sessionRepository.save(session);
        return new SessionResponse(session);
    }
}