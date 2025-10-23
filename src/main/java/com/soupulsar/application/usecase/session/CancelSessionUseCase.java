package com.soupulsar.application.usecase.session;

import com.soupulsar.application.dto.response.SessionResponse;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.SessionRepository;
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