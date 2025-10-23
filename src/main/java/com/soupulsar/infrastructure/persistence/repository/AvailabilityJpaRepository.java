package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.availability.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface AvailabilityJpaRepository extends JpaRepository<AvailabilityEntity, Long> {

    List<AvailabilityEntity> findBySpecialistId(UUID specialistId);
    List<AvailabilityEntity> findBySpecialistIdAndDayOfWeek(UUID specialistId, DayOfWeek dayOfWeek);
}
