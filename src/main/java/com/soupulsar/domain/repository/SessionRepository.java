package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.session.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {

    Map<UUID, Long> countCompletedSessionsBySpecialistIds(List<UUID> specialistIds);

    Long countCompletedSessionsBySpecialistId(UUID specialistId);

    List<Session> findBySpecialistIdAndDate(UUID specialistId, LocalDate date);

    Session save(Session session);

    Optional<Session> findBySessionId(UUID sessionId);

    List<Session> findOverlappingSessions(UUID uuid, LocalDateTime startAt, LocalDateTime endAt);
}
