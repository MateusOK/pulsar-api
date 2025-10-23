package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.repository.ClientProfileRepository;
import com.soupulsar.infrastructure.persistence.entity.client.ClientProfileEntity;
import com.soupulsar.infrastructure.persistence.mapper.client.ClientProfileMapper;
import com.soupulsar.infrastructure.persistence.repository.ClientProfileJpaRepository;
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