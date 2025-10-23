package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.client.ClientProfile;

import java.util.Optional;
import java.util.UUID;

public interface ClientProfileRepository {

    ClientProfile save(ClientProfile profile);

    Optional<ClientProfile> findById(UUID id);

}
