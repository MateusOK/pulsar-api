package com.soupulsar.domain.repository;

import com.soupulsar.application.specialist.calendar.CalendarSessionResponse;
import com.soupulsar.application.specialist.shared.daterange.DateRange;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.session.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public interface SessionRepository {

    Map<UUID, Long> countCompletedSessionsBySpecialistIds(List<UUID> specialistIds);

    Long countCompletedSessionsBySpecialistId(UUID specialistId);

    List<Session> findBySpecialistIdAndDate(UUID specialistId, LocalDate date);

    Session save(Session session);

    Optional<Session> findBySessionId(UUID sessionId);

    List<Session> findOverlappingSessions(UUID uuid, LocalDateTime startAt, LocalDateTime endAt);

    Optional<Session> findNextSession(UUID specialistId, LocalDateTime currentDateTime);

    Long countSessionsByStatus(UUID specialistId, Collection<SessionStatus> statuses, LocalDateTime startAt, LocalDateTime endAt);

    List<Session> findBySpecialistIdAndPeriod(UUID specialistId, DateRange period);

    List<CalendarSessionResponse> findCalendarSessions(UUID specialistId, DateRange period);

    Optional<Session> findBySessionIdAndSpecialistId(UUID sessionId, UUID specialistId);
}
