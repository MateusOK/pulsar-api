package com.soupulsar.modulith.scheduling.domain.repository;

import com.soupulsar.modulith.scheduling.domain.model.Session;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SessionRepository {

    Session save(Session session);

    List<Session> findOverlappingSessions(UUID uuid, LocalDateTime startAt, LocalDateTime endAt);
}
