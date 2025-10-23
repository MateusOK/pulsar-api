package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.specialist.SpecialistProfile;

import java.util.Optional;
import java.util.UUID;

public interface SpecialistProfileRepository {

    SpecialistProfile save(SpecialistProfile profile);
    Optional<SpecialistProfile> findById(UUID id);

}
