package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.availability.Availability;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface AvailabilityRepository {

    Availability save(Availability availability);
    List<Availability> findBySpecialistId(UUID specialistId);
    List<Availability> findBySpecialistIdAndDayOfWeek(UUID specialistId, DayOfWeek dayOfWeek);
}