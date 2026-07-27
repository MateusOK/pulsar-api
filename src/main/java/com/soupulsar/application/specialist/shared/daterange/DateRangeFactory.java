package com.soupulsar.application.specialist.shared.daterange;

import com.soupulsar.application.specialist.calendar.CalendarView;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DateRangeFactory {

    public DateRange today(LocalDate referenceDate) {
        return new DateRange(
                referenceDate.atStartOfDay(),
                referenceDate.atTime(LocalTime.MAX)
        );
    }

    public DateRange week(LocalDate referenceDate) {
        return new DateRange(
                referenceDate.with(java.time.DayOfWeek.MONDAY).atStartOfDay(),
                referenceDate.with(java.time.DayOfWeek.SUNDAY).atTime(LocalTime.MAX)
        );
    }

    public DateRange month(LocalDate referenceDate) {
        return new DateRange(
                referenceDate.withDayOfMonth(1).atStartOfDay(),
                referenceDate.withDayOfMonth(referenceDate.lengthOfMonth()).atTime(LocalTime.MAX)
        );
    }

    public DateRange from(CalendarView view, LocalDate referenceDate) {
        return switch (view) {
            case DAY -> today(referenceDate);
            case WEEK -> week(referenceDate);
            case MONTH -> month(referenceDate);
        };
    }
}