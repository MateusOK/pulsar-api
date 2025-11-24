package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.session.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, Long> {

    @Query("""
    SELECT s FROM SessionEntity s
    WHERE s.specialistId = :specialistId
    AND s.startAt < :end
    AND s.endAt > :start
""")
    List<SessionEntity> findOverlappingSessions(@Param("specialistId") UUID specialistId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
        SELECT s.specialistId, COUNT(s)
        FROM SessionEntity s
        WHERE s.status = 'COMPLETED'
        AND s.specialistId IN :specialistIds
        GROUP BY s.specialistId
        """)
    List<Object[]> countCompletedSessionsBySpecialistIds(@Param("specialistIds") List<UUID> specialistIds);

    @Query("""
        SELECT COUNT(s)
        FROM SessionEntity s
        WHERE s.specialistId = :specialistId
        AND s.status = 'COMPLETED'
        """)
    Long countCompletedSessionsBySpecialistId(@Param("specialistId") UUID specialistId);

    @Query("""
        SELECT s FROM SessionEntity s
        WHERE s.specialistId = :specialistId
        AND CAST(s.startAt AS date) = :date
    """)
    List<SessionEntity> findBySpecialistIdAndDate(@Param("specialistId") UUID specialistId, @Param("date") LocalDate date);

    Optional<SessionEntity> findBySessionId(UUID sessionId);
}
