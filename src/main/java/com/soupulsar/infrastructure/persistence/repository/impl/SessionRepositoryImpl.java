package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.infrastructure.persistence.repository.SessionJpaRepository;
import com.soupulsar.infrastructure.persistence.entity.session.SessionEntity;
import com.soupulsar.infrastructure.persistence.mapper.SessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionJpaRepository jpaRepository;

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