package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.availability.AvailabilityBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AvailbilityBlockJpaRepository extends JpaRepository<AvailabilityBlockEntity, UUID> {

    List<AvailabilityBlockEntity> findAllBySpecialistId(java.util.UUID specialistId);

    boolean existsOverlappingBlockBySpecialistIdAndStartsAtLessThanAndEndsAtGreaterThan(UUID specialistId, LocalDateTime endsAt, LocalDateTime startsAt);
}