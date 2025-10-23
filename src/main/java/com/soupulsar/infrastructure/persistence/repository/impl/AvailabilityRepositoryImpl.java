package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
import com.soupulsar.infrastructure.persistence.repository.AvailabilityJpaRepository;
import com.soupulsar.infrastructure.persistence.entity.availability.AvailabilityEntity;
import com.soupulsar.infrastructure.persistence.mapper.AvailabilityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class AvailabilityRepositoryImpl implements AvailabilityRepository {

    private final AvailabilityJpaRepository jpaRepository;

    @Override
    public Availability save(Availability availability) {
        AvailabilityEntity entity = AvailabilityMapper.toEntity(availability);
        AvailabilityEntity saved = jpaRepository.save(entity);
        return AvailabilityMapper.toModel(saved);
    }

    @Override
    public List<Availability> findBySpecialistId(UUID specialistId) {
        return jpaRepository.findBySpecialistId(specialistId)
                .stream()
                .map(AvailabilityMapper::toModel)
                .toList();
    }

    @Override
    public List<Availability> findBySpecialistIdAndDayOfWeek(UUID specialistId, DayOfWeek dayOfWeek) {
        return jpaRepository.findBySpecialistIdAndDayOfWeek(specialistId, dayOfWeek)
                .stream()
                .map(AvailabilityMapper::toModel)
                .toList();
    }
}