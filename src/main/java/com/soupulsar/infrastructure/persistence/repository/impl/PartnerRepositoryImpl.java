package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.partner.Partner;
import com.soupulsar.domain.repository.PartnerRepository;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerEntity;
import com.soupulsar.infrastructure.persistence.mapper.partner.PartnerMapper;
import com.soupulsar.infrastructure.persistence.repository.PartnerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PartnerRepositoryImpl implements PartnerRepository {

    private final PartnerJpaRepository jpaRepository;

    @Override
    public List<Partner> findAllByIds(Set<UUID> ids) {
        return jpaRepository.findAllByIdIn(ids).stream().map(PartnerMapper::toModel).toList();
    }

    @Override
    public Optional<Partner> findById(UUID id) {
        return jpaRepository.findById(id).map(PartnerMapper::toModel);
    }

    @Override
    public Partner save(Partner partner) {
        PartnerEntity entity = PartnerMapper.toEntity(partner);
        PartnerEntity saved = jpaRepository.save(entity);
        return PartnerMapper.toModel(saved);
    }
}