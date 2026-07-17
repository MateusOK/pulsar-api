package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.session.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public interface SessionRepository {

    Map<UUID, Long> countCompletedSessionsBySpecialistIds(List<UUID> specialistIds);

    Long countCompletedSessionsBySpecialistId(UUID specialistId);

    List<Session> findBySpecialistIdAndDate(UUID specialistId, LocalDate date);

    Session save(Session session);

    Optional<Session> findBySessionId(UUID sessionId);

    List<Session> findOverlappingSessions(UUID uuid, LocalDateTime startAt, LocalDateTime endAt);

    Optional<Session> findNextSession(UUID specialistId, LocalDateTime currentDateTime);

    Long countSessionsByStatus(UUID specialistId, Collection<SessionStatus> statuses, LocalDateTime startAt, LocalDateTime endAt);
}
