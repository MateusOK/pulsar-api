package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.specialist.SpecialistProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialistProfileJpaRepository extends JpaRepository<SpecialistProfileEntity, UUID> {
}
