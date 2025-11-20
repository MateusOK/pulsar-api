package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface SpecialistProfileRepository {

    SpecialistProfile save(SpecialistProfile profile);
    Optional<SpecialistProfile> findById(UUID id);
    Page<SpecialistProfile> findAll(SpecialistType type, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
