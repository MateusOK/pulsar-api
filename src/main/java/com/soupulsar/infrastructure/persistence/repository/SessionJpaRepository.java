package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.application.specialist.calendar.CalendarSessionResponse;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.infrastructure.persistence.entity.session.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, UUID> {

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

    Optional<SessionEntity> findFirstBySpecialistIdAndStatusAndStartAtGreaterThanEqualOrderByStartAtAsc(UUID specialistId, SessionStatus status, LocalDateTime startAt);

    Long countBySpecialistIdAndStatusInAndStartAtBetween(UUID specialistId, Collection<SessionStatus> statuses, LocalDateTime startAt, LocalDateTime endAt);

    List<SessionEntity> findBySpecialistIdAndStartAtBetweenOrderByStartAtAsc(UUID specialistId, LocalDateTime startAt, LocalDateTime endAt);

    @Query("""
        SELECT new com.soupulsar.application.specialist.calendar.CalendarSessionResponse(
            s.sessionId,
            s.startAt,
            s.endAt,
            s.status,
            s.clientId,
            u.name
        )
        FROM SessionEntity s
        JOIN UserEntity u
             ON u.userId = s.clientId
        WHERE s.specialistId = :specialistId
        AND s.startAt BETWEEN :startAt AND :endAt
        ORDER BY s.startAt ASC
       """)
    List<CalendarSessionResponse> findCalendarSessions(UUID specialistId, LocalDateTime startAt, LocalDateTime endAt);

    Optional<SessionEntity> findBySessionIdAndSpecialistId(UUID sessionId, UUID specialistId);


    @Query(value = """
        SELECT COUNT(*)
        FROM sessions s
        WHERE s.specialist_id = :specialistId
                AND s.status = 'CONFIRMED'
                AND s.start_at >= CURRENT_TIMESTAMP
                AND EXTRACT(ISODOW FROM s.start_at) = :dayOfWeek
                AND CAST(s.start_at AS time) < :endTime
                AND CAST(s.end_at AS time) > :startTime
        """, nativeQuery = true)
    long countConflictingFutureSessionsForAvailability(@Param("specialistId") UUID specialistId, @Param("dayOfWeek") DayOfWeek dayOfWeek, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Query(value = """
        SELECT COUNT(*)
        FROM SessionEntity s
        WHERE s.specialistId = :specialistId
                AND s.status = 'CONFIRMED'
                AND s.startAt >= CURRENT_TIMESTAMP
                AND s.startAt < :endAt
                AND s.endAt > :startAt
        """)
    long countConflictingFutureSessionsForAvailabilityBlocks(UUID specialistId, LocalDateTime startAt, LocalDateTime endAt);
}