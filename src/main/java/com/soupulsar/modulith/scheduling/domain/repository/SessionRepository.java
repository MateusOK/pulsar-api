package com.soupulsar.modulith.scheduling.domain.repository;

import com.soupulsar.modulith.scheduling.domain.model.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {

    Session save(Session session);

    Optional<Session> findBySessionId(UUID sessionId);

    List<Session> findOverlappingSessions(UUID uuid, LocalDateTime startAt, LocalDateTime endAt);
}
