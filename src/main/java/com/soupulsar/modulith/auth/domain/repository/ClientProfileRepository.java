package com.soupulsar.modulith.auth.domain.repository;

import com.soupulsar.modulith.auth.domain.model.ClientProfile;

import java.util.Optional;
import java.util.UUID;

public interface ClientProfileRepository {

    ClientProfile save(ClientProfile profile);

    Optional<ClientProfile> findById(UUID id);

}
