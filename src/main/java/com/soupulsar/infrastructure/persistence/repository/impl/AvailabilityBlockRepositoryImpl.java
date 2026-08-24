package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.availability.AvailabilityBlock;
import com.soupulsar.domain.repository.AvailabilityBlockRepository;
import com.soupulsar.infrastructure.persistence.entity.availability.AvailabilityBlockEntity;
import com.soupulsar.infrastructure.persistence.mapper.availability.AvailabilityBlockMapper;
import com.soupulsar.infrastructure.persistence.repository.AvailbilityBlockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AvailabilityBlockRepositoryImpl implements AvailabilityBlockRepository {

    private final AvailbilityBlockJpaRepository jpaRepository;

    @Override
    public AvailabilityBlock save(AvailabilityBlock availabilityBlock) {
        AvailabilityBlockEntity entity = AvailabilityBlockMapper.toEntity(availabilityBlock);
        AvailabilityBlockEntity saved = jpaRepository.save(entity);
        return AvailabilityBlockMapper.toModel(saved);
    }

    @Override
    public List<AvailabilityBlock> findAllBySpecialistId(UUID specialistId) {
        return jpaRepository.findAllBySpecialistId(specialistId).stream()
                .map(AvailabilityBlockMapper::toModel)
                .toList();
    }

    @Override
    public Optional<AvailabilityBlock> findById(UUID id) {
        return jpaRepository.findById(id).map(AvailabilityBlockMapper::toModel);
    }

    @Override
    public void delete(AvailabilityBlock availabilityBlock) {
        jpaRepository.delete(AvailabilityBlockMapper.toEntity(availabilityBlock));
    }
}