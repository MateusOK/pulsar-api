package com.soupulsar.modulith.auth.domain.repository;

import com.soupulsar.modulith.auth.domain.model.SpecialistProfile;

import java.util.Optional;
import java.util.UUID;

public interface SpecialistProfileRepository {

    SpecialistProfile save(SpecialistProfile profile);
    Optional<SpecialistProfile> findById(UUID id);

}
