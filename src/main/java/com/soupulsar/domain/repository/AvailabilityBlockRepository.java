package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.availability.AvailabilityBlock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilityBlockRepository {

    AvailabilityBlock save(AvailabilityBlock availabilityBlock);
    List<AvailabilityBlock> findAllBySpecialistId(UUID specialistId);
    Optional<AvailabilityBlock> findById(UUID id);
    void delete(AvailabilityBlock availabilityBlock);
}
