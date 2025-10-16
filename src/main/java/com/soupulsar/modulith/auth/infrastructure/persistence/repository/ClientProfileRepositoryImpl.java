package com.soupulsar.modulith.auth.infrastructure.persistence.repository;

import com.soupulsar.modulith.auth.domain.model.ClientProfile;
import com.soupulsar.modulith.auth.domain.repository.ClientProfileRepository;
import com.soupulsar.modulith.auth.infrastructure.persistence.entity.ClientProfileEntity;
import com.soupulsar.modulith.auth.infrastructure.persistence.mapper.ClientProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClientProfileRepositoryImpl implements ClientProfileRepository {

    private final ClientProfileJpaRepository jpaRepository;

    @Override
    public ClientProfile save(ClientProfile profile) {
        ClientProfileEntity entity = ClientProfileMapper.toEntity(profile);
        ClientProfileEntity saved = jpaRepository.save(entity);

        return ClientProfileMapper.toModel(saved);
    }

    @Override
    public Optional<ClientProfile> findById(UUID id) {
        return jpaRepository.findById(id).map(ClientProfileMapper::toModel);
    }
}