package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.partner.ClientPartnerBenefit;
import com.soupulsar.domain.repository.ClientPartnerBenefitRepository;
import com.soupulsar.infrastructure.persistence.entity.partner.ClientPartnerBenefitEntity;
import com.soupulsar.infrastructure.persistence.mapper.partner.ClientPartnerBenefitMapper;
import com.soupulsar.infrastructure.persistence.repository.ClientPartnerBenefitJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClientPartnerBenefitRepositoryImpl implements ClientPartnerBenefitRepository {

    private final ClientPartnerBenefitJpaRepository jpaRepository;

    @Override
    public Set<UUID> findPartnerIdsWithExistingBenefit(UUID clientId, Set<UUID> partnerIds) {
        return jpaRepository.findPartnerIdsWithExistingBenefit(clientId, partnerIds);
    }

    @Override
    public ClientPartnerBenefit save(ClientPartnerBenefit benefit) {
        ClientPartnerBenefitEntity entity = ClientPartnerBenefitMapper.toEntity(benefit);
        ClientPartnerBenefitEntity saved = jpaRepository.save(entity);
        return ClientPartnerBenefitMapper.toModel(saved);
    }
}