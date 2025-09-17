package com.soupulsar.modulith.scheduling.application.usecase;

import com.soupulsar.modulith.scheduling.domain.model.Session;
import com.soupulsar.modulith.scheduling.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ConfirmSessionUseCase {

    private final SessionRepository sessionRepository;

    public void execute(UUID sessionId) {
        Session session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        session.confirmPayment();
        sessionRepository.save(session);
    }
}