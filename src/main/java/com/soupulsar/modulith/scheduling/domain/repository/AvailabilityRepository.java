package com.soupulsar.modulith.scheduling.domain.repository;

import com.soupulsar.modulith.scheduling.domain.model.Availability;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface AvailabilityRepository {

    Availability save(Availability availability);
    List<Availability> findBySpecialistId(UUID specialistId);
    List<Availability> findBySpecialistIdAndDayOfWeek(UUID specialistId, DayOfWeek dayOfWeek);
}