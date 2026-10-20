package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.partner.PartnerEligibility;
import com.soupulsar.domain.model.partner.PartnerEligibilityType;
import com.soupulsar.domain.repository.PartnerEligibilityRepository;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerEligibilityEntity;
import com.soupulsar.infrastructure.persistence.mapper.partner.PartnerEligibilityMapper;
import com.soupulsar.infrastructure.persistence.repository.PartnerEligibilityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PartnerEligibilityRepositoryImpl implements PartnerEligibilityRepository {

    private final PartnerEligibilityJpaRepository jpaRepository;


    @Override
    public List<PartnerEligibility> findMatchingEligibility(String email, String domain) {
        return jpaRepository.findMatchingEligibility(PartnerEligibilityType.EMAIL, PartnerEligibilityType.DOMAIN, email, domain)
                .stream()
                .map(PartnerEligibilityMapper::toModel)
                .toList();
    }

    @Override
    public PartnerEligibility save(PartnerEligibility partnerEligibility) {
        PartnerEligibilityEntity entity = PartnerEligibilityMapper.toEntity(partnerEligibility);
        PartnerEligibilityEntity saved = jpaRepository.save(entity);
        return PartnerEligibilityMapper.toModel(saved);
    }
}