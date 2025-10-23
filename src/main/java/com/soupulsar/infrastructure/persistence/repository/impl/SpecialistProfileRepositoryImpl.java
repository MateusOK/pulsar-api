package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.infrastructure.persistence.entity.specialist.SpecialistProfileEntity;
import com.soupulsar.infrastructure.persistence.mapper.specialist.SpecialistProfileMapper;
import com.soupulsar.infrastructure.persistence.repository.SpecialistProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SpecialistProfileRepositoryImpl implements SpecialistProfileRepository {

    private final SpecialistProfileJpaRepository jpaRepository;

    @Override
    public SpecialistProfile save(SpecialistProfile profile) {
        SpecialistProfileEntity entity = SpecialistProfileMapper.toEntity(profile);
        SpecialistProfileEntity saved = jpaRepository.save(entity);
        return SpecialistProfileMapper.toModel(saved);
    }

    @Override
    public Optional<SpecialistProfile> findById(UUID id) {
        return Optional.empty();
    }
}
