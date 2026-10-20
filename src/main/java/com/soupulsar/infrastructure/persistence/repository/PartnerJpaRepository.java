package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.partner.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PartnerJpaRepository extends JpaRepository<PartnerEntity, UUID> {

    List<PartnerEntity> findAllByIdIn(Set<UUID> ids);

}
