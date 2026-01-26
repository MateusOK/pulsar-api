package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.specialist.SpecialistPayoutAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpecialistPayoutAccountJpaRepository extends JpaRepository<SpecialistPayoutAccountEntity, UUID> {

    Optional<SpecialistPayoutAccountEntity> findBySpecialistId(UUID specialistId);
}
