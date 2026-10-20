package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.partner.PartnerDiscountCode;
import com.soupulsar.domain.model.partner.PartnerDiscountCodeStatus;
import com.soupulsar.domain.repository.PartnerDiscountCodeRepository;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerDiscountCodeEntity;
import com.soupulsar.infrastructure.persistence.mapper.partner.PartnerDiscountCodeMapper;
import com.soupulsar.infrastructure.persistence.repository.PartnerDiscountCodeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PartnerDiscountCodeRepositoryImpl implements PartnerDiscountCodeRepository {

    private final PartnerDiscountCodeJpaRepository jpaRepository;


    @Override
    public PartnerDiscountCode save(PartnerDiscountCode partnerDiscountCode) {
        PartnerDiscountCodeEntity entity = PartnerDiscountCodeMapper.toEntity(partnerDiscountCode);
        PartnerDiscountCodeEntity saved = jpaRepository.save(entity);
        return PartnerDiscountCodeMapper.toModel(saved);
    }

    @Override
    public void invalidateActiveCodes(UUID partnerDiscountId, UUID clientId) {
        jpaRepository.invalidateActiveCodes(partnerDiscountId, clientId, PartnerDiscountCodeStatus.ACTIVE, PartnerDiscountCodeStatus.INVALIDATED);
    }

    @Override
    public Optional<PartnerDiscountCode> findByClientIdAndCodeHash(UUID clientId, String codeHash) {
        return jpaRepository.findByClientIdAndCodeHash(clientId, codeHash).map(PartnerDiscountCodeMapper::toModel);
    }
}