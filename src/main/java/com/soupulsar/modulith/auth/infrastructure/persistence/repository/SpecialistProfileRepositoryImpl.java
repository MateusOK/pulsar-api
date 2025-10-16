package com.soupulsar.modulith.auth.infrastructure.persistence.repository;

import com.soupulsar.modulith.auth.domain.model.SpecialistProfile;
import com.soupulsar.modulith.auth.domain.repository.SpecialistProfileRepository;
import com.soupulsar.modulith.auth.infrastructure.persistence.entity.SpecialistProfileEntity;
import com.soupulsar.modulith.auth.infrastructure.persistence.mapper.SpecialistProfileMapper;
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
