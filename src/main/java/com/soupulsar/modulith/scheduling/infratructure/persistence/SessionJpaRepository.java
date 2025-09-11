package com.soupulsar.modulith.scheduling.infratructure.persistence;

import com.soupulsar.modulith.scheduling.infratructure.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, Long> {

    List<SessionEntity> findBySpecialistIdAndTimeRange(UUID uuid, LocalDateTime startAt, LocalDateTime endAt);
}
