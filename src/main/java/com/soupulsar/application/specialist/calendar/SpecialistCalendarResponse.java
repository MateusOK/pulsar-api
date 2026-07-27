package com.soupulsar.application.specialist.calendar;

import lombok.Builder;

import java.util.List;

@Builder
public record SpecialistCalendarResponse(

        Long todayTotalSessions,
        Long weekTotalSessions,
        Long monthTotalSessions,

        List<CalendarSessionResponse> sessions
) {
}