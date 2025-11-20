package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.specialist.SpecialistProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SpecialistProfileJpaRepository extends JpaRepository<SpecialistProfileEntity, Long>, JpaSpecificationExecutor<SpecialistProfileEntity> {

    Optional<SpecialistProfileEntity> findByUserId(UUID userId);

}