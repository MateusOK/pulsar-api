package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.enums.SessionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionResponse(

        UUID sessionId,
        UUID specialistId,
        UUID clientId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        SessionStatus status

) {
    public SessionResponse(Session session){
        this(
                session.getSessionId(),
                session.getSpecialistId(),
                session.getClientId(),
                session.getStartAt(),
                session.getEndAt(),
                session.getStatus()
        );
    }
}