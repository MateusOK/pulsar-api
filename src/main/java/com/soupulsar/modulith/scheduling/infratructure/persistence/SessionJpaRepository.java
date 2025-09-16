package com.soupulsar.modulith.scheduling.infratructure.persistence;

import com.soupulsar.modulith.scheduling.infratructure.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, Long> {

    @Query("""
    SELECT s FROM SessionEntity s
    WHERE s.specialistId = :specialistId
    AND s.startAt < :end
    AND s.endAt > :start
""")
    List<SessionEntity> findOverlappingSessions(UUID specialistId, LocalDateTime start, LocalDateTime end);

}
