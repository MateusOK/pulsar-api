package com.soupulsar.domain.model.availability;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Represents the availability of a specialist for scheduling sessions.
 * Example: available time slots, days off, etc.
 */

@Getter
public class Availability {

    private final UUID id;
    private final UUID specialistId;
    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Availability(UUID id, UUID specialistId, DayOfWeek dayOfWeek,
                             LocalTime startTime, LocalTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        this.id = id;
        this.specialistId = specialistId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Checks if a given time falls within the availability period.
     */
    public boolean isWithinAvailability(DayOfWeek day, LocalTime time) {
        return this.dayOfWeek == day && !time.isBefore(startTime) && !time.isAfter(endTime);
    }

}