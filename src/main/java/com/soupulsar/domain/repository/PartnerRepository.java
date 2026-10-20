package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.partner.Partner;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PartnerRepository {

    List<Partner> findAllByIds(Set<UUID> ids);

    Optional<Partner> findById(UUID id);

    Partner save(Partner partner);

}