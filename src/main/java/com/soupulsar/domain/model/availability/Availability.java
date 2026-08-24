package com.soupulsar.domain.model.availability;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Represents the availability of a specialist for scheduling sessions.
 * Example: available time slots, days off, etc.
 */

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Availability {

    private final UUID id;
    private final UUID specialistId;
    private final DayOfWeek dayOfWeek;
    private boolean enabled;
    private LocalTime startTime;
    private LocalTime endTime;


    public static Availability create(UUID specialistId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        validate(startTime, endTime);
        return new AvailabilityBuilder()
                .id(UUID.randomUUID())
                .specialistId(specialistId)
                .dayOfWeek(dayOfWeek)
                .enabled(true)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public static Availability restore(UUID id, UUID specialistId, DayOfWeek dayOfWeek, boolean enabled, LocalTime startTime, LocalTime endTime) {
        return new Availability(id, specialistId, dayOfWeek, enabled, startTime, endTime);
    }

    public void update(LocalTime startTime, LocalTime endTime) {
        validate(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void enable(){
        this.enabled = true;
    }

    public void disable(){
        this.enabled = false;
    }

    public boolean overlaps(Availability other) {

        if (!this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }
        return startTime.isBefore(other.endTime) && endTime.isAfter(other.startTime);
    }

    private static void validate(LocalTime startTime, LocalTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }
}