package com.soupulsar.application.usecase.specialist;

import com.soupulsar.application.specialist.calendar.CalendarSessionResponse;
import com.soupulsar.application.specialist.calendar.CalendarView;
import com.soupulsar.application.specialist.calendar.GetSpecialistCalendarUseCase;
import com.soupulsar.application.specialist.calendar.SpecialistCalendarResponse;
import com.soupulsar.application.specialist.shared.daterange.DateRange;
import com.soupulsar.application.specialist.shared.daterange.DateRangeFactory;
import com.soupulsar.application.specialist.shared.summary.SessionStatisticsCalculator;
import com.soupulsar.application.specialist.shared.summary.SessionSummary;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.repository.SessionRepository;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetSpecialistCalendarUseCaseTest {

    @Test
    void shouldReturnCalendarSuccessfully() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);
        SessionStatisticsCalculator sessionStatisticsCalculator = mock(SessionStatisticsCalculator.class);
        DateRangeFactory dateRangeFactory = mock(DateRangeFactory.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 22, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        DateRange calendarRange = new DateRange(now.minusDays(1), now.plusDays(1));
        when(dateRangeFactory.from(CalendarView.WEEK, now.toLocalDate())).thenReturn(calendarRange);

        UUID sessionId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        CalendarSessionResponse calendarSession = new CalendarSessionResponse(sessionId, now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED, clientId, "Alice");

        when(sessionRepository.findCalendarSessions(specialistId, calendarRange)).thenReturn(List.of(calendarSession));

        // totals
        SessionSummary todaySummary = new SessionSummary(1L, 2L);
        SessionSummary weekSummary = new SessionSummary(3L, 4L);
        SessionSummary monthSummary = new SessionSummary(5L, 6L);
        when(sessionStatisticsCalculator.calculate(eq(specialistId), any())).thenReturn(todaySummary).thenReturn(weekSummary).thenReturn(monthSummary);

        GetSpecialistCalendarUseCase useCase = new GetSpecialistCalendarUseCase(securityUtils, sessionRepository, sessionStatisticsCalculator, dateRangeFactory, clock);

        SpecialistCalendarResponse response = useCase.execute(CalendarView.WEEK);

        assertNotNull(response);
        assertEquals(3L, response.todayTotalSessions()); // from SessionSummary.total(): 1+2
        assertEquals(7L, response.weekTotalSessions()); // 3+4
        assertEquals(11L, response.monthTotalSessions()); // 5+6
        assertNotNull(response.sessions());
        assertEquals(1, response.sessions().size());
        assertEquals(sessionId, response.sessions().get(0).sessionId());
    }

    @Test
    void shouldReturnEmptyCalendarWhenNoSessions() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);
        SessionStatisticsCalculator sessionStatisticsCalculator = mock(SessionStatisticsCalculator.class);
        DateRangeFactory dateRangeFactory = mock(DateRangeFactory.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 22, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        DateRange calendarRange = new DateRange(now, now.plusDays(1));
        when(dateRangeFactory.from(CalendarView.DAY, now.toLocalDate())).thenReturn(calendarRange);

        when(sessionRepository.findCalendarSessions(specialistId, calendarRange)).thenReturn(List.of());

        when(sessionStatisticsCalculator.calculate(any(), any())).thenReturn(new SessionSummary(0L, 0L));

        GetSpecialistCalendarUseCase useCase = new GetSpecialistCalendarUseCase(securityUtils, sessionRepository, sessionStatisticsCalculator, dateRangeFactory, clock);

        SpecialistCalendarResponse response = useCase.execute(CalendarView.DAY);

        assertNotNull(response);
        assertEquals(0L, response.todayTotalSessions());
        assertEquals(0L, response.weekTotalSessions());
        assertEquals(0L, response.monthTotalSessions());
        assertNotNull(response.sessions());
        assertEquals(0, response.sessions().size());
    }
}
