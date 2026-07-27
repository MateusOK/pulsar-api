package com.soupulsar.application.specialist.calendar;

import com.soupulsar.application.specialist.shared.daterange.DateRange;
import com.soupulsar.application.specialist.shared.daterange.DateRangeFactory;
import com.soupulsar.application.specialist.shared.summary.SessionStatisticsCalculator;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class GetSpecialistCalendarUseCase {

    private final SecurityUtils  securityUtils;
    private final SessionRepository sessionRepository;
    private final SessionStatisticsCalculator sessionStatisticsCalculator;
    private final DateRangeFactory dateRangeFactory;
    private final Clock clock;

    public SpecialistCalendarResponse execute(CalendarView calendarView) {

        UUID specialistId = securityUtils.getCurrentUserId();
        LocalDateTime now = LocalDateTime.now(clock);

        DateRange dateRange = dateRangeFactory.from(calendarView, now.toLocalDate());
        List<CalendarSessionResponse> sessions = sessionRepository.findCalendarSessions(specialistId, dateRange);

        return SpecialistCalendarResponse.builder()
                .sessions(sessions)
                .todayTotalSessions(getTotalSessions(specialistId, dateRangeFactory.today(now.toLocalDate())))
                .weekTotalSessions(getTotalSessions(specialistId, dateRangeFactory.week(now.toLocalDate())))
                .monthTotalSessions(getTotalSessions(specialistId, dateRangeFactory.month(now.toLocalDate())))
                .build();
    }

    private long getTotalSessions(UUID specialistId, DateRange dateRange) {
        return sessionStatisticsCalculator.calculate(specialistId, dateRange).total();
    }
}