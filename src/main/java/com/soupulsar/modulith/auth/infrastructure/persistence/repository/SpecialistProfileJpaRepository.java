package com.soupulsar.modulith.auth.infrastructure.persistence.repository;

import com.soupulsar.modulith.auth.infrastructure.persistence.entity.SpecialistProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialistProfileJpaRepository extends JpaRepository<SpecialistProfileEntity, UUID> {
}
