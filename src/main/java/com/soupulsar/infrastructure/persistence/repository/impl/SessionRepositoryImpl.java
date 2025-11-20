package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.infrastructure.persistence.repository.SessionJpaRepository;
import com.soupulsar.infrastructure.persistence.entity.session.SessionEntity;
import com.soupulsar.infrastructure.persistence.mapper.SessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
@Repository
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionJpaRepository jpaRepository;

    @Override
    public Map<UUID, Long> countCompletedSessionsBySpecialistIds(List<UUID> specialistIds) {
        if (specialistIds == null || specialistIds.isEmpty()) return Collections.emptyMap();

        List<Object[]> results = jpaRepository.countCompletedSessionsBySpecialistIds(specialistIds);

        return results.stream().collect(Collectors.toMap(
                row -> (UUID) row[0],
                row -> (Long) row[1]
        ));
    }

    @Override
    public Long countCompletedSessionsBySpecialistId(UUID specialistId) {
        if (specialistId == null) return 0L;

        return jpaRepository.countCompletedSessionsBySpecialistId(specialistId);
    }

    @Override
    public List<Session> findBySpecialistIdAndDate(UUID specialistId, LocalDate date) {
        if (specialistId == null) return Collections.emptyList();

        return jpaRepository.findBySpecialistIdAndDate(specialistId, date)
                .stream()
                .map(SessionMapper::toModel)
                .toList();
    }

    @Override
    public Session save(Session session) {
        SessionEntity entity = SessionMapper.toEntity(session);
        SessionEntity saved = jpaRepository.save(entity);
        return SessionMapper.toModel(saved);
    }

    @Override
    public Optional<Session> findBySessionId(UUID sessionId) {
        return jpaRepository.findBySessionId(sessionId)
                .map(SessionMapper::toModel);
    }

    @Override
    public List<Session> findOverlappingSessions(UUID uuid, LocalDateTime startAt, LocalDateTime endAt) {
        return jpaRepository.findOverlappingSessions(uuid, startAt, endAt)
                .stream()
                .map(SessionMapper::toModel)
                .toList();
    }
}