package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.partner.PartnerDiscount;
import com.soupulsar.domain.repository.PartnerDiscountRepository;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerDiscountEntity;
import com.soupulsar.infrastructure.persistence.mapper.partner.PartnerDiscountMapper;
import com.soupulsar.infrastructure.persistence.repository.PartnerDiscountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PartnerDiscountRepositoryImpl implements PartnerDiscountRepository {

    private final PartnerDiscountJpaRepository jpaRepository;

    @Override
    public List<PartnerDiscount> findAllByIds(Set<UUID> ids) {
        return jpaRepository.findAllById(ids).stream().map(PartnerDiscountMapper::toModel).toList();
    }

    @Override
    public Optional<PartnerDiscount> findById(UUID id) {
        return jpaRepository.findById(id).map(PartnerDiscountMapper::toModel);
    }

    @Override
    public PartnerDiscount save(PartnerDiscount partnerDiscount) {
        PartnerDiscountEntity entity = PartnerDiscountMapper.toEntity(partnerDiscount);
        PartnerDiscountEntity saved = jpaRepository.save(entity);
        return PartnerDiscountMapper.toModel(saved);
    }
}