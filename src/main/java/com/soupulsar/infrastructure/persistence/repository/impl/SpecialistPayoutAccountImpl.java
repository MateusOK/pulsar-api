package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.payment.SpecialistPayoutAccount;
import com.soupulsar.domain.repository.SpecialistPayoutAccountRepository;
import com.soupulsar.infrastructure.persistence.entity.specialist.SpecialistPayoutAccountEntity;
import com.soupulsar.infrastructure.persistence.mapper.specialist.SpecialistPayoutAccountMapper;
import com.soupulsar.infrastructure.persistence.repository.SpecialistPayoutAccountJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class SpecialistPayoutAccountImpl implements SpecialistPayoutAccountRepository {

    private final SpecialistPayoutAccountJpaRepository repository;

    @Override
    public Optional<SpecialistPayoutAccount> findById(UUID id) {
        return repository.findById(id).map(SpecialistPayoutAccountMapper::toModel);
    }

    @Override
    public Optional<SpecialistPayoutAccount> findBySpecialistId(UUID specialistId) {
        return repository.findBySpecialistId(specialistId).map(SpecialistPayoutAccountMapper::toModel);
    }

    @Override
    public SpecialistPayoutAccount save(SpecialistPayoutAccount specialistPayoutAccount) {
        SpecialistPayoutAccountEntity entity = SpecialistPayoutAccountMapper.toEntity(specialistPayoutAccount);
        SpecialistPayoutAccountEntity saved = repository.save(entity);
        return SpecialistPayoutAccountMapper.toModel(saved);
    }
}
