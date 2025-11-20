package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.infrastructure.persistence.entity.specialist.SpecialistProfileEntity;
import com.soupulsar.infrastructure.persistence.mapper.specialist.SpecialistProfileMapper;
import com.soupulsar.infrastructure.persistence.repository.SpecialistProfileJpaRepository;
import com.soupulsar.infrastructure.persistence.specification.SpecialistSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
        return jpaRepository.findByUserId(id).map(SpecialistProfileMapper::toModel);
    }

    @Override
    public Page<SpecialistProfile> findAll(SpecialistType type, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<SpecialistProfileEntity> spec = Specification.unrestricted();

        if (type != null) spec = spec.and(SpecialistSpecifications.hasType(type));

        if (minPrice != null) spec = spec.and(SpecialistSpecifications.hasPriceRange(minPrice, null));

        if (maxPrice != null) spec = spec.and(SpecialistSpecifications.hasPriceRange(null, maxPrice));

        return jpaRepository.findAll(spec, pageable).map(SpecialistProfileMapper::toModel);
    }
}