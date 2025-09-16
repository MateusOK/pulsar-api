package com.soupulsar.modulith.scheduling.infratructure.persistence.repository;

import com.soupulsar.modulith.scheduling.domain.model.Session;
import com.soupulsar.modulith.scheduling.domain.repository.SessionRepository;
import com.soupulsar.modulith.scheduling.infratructure.persistence.SessionJpaRepository;
import com.soupulsar.modulith.scheduling.infratructure.persistence.entity.SessionEntity;
import com.soupulsar.modulith.scheduling.infratructure.persistence.mapper.SessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

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
    public List<Session> findOverlappingSessions(UUID uuid, LocalDateTime startAt, LocalDateTime endAt) {
        return jpaRepository.findOverlappingSessions(uuid, startAt, endAt)
                .stream()
                .map(SessionMapper::toModel)
                .toList();
    }
}