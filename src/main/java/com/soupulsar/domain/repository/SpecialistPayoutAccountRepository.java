package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.payment.SpecialistPayoutAccount;

import java.util.Optional;
import java.util.UUID;

public interface SpecialistPayoutAccountRepository {

    Optional<SpecialistPayoutAccount> findById(UUID id);
    Optional<SpecialistPayoutAccount> findBySpecialistId(UUID specialistId);
    SpecialistPayoutAccount save(SpecialistPayoutAccount specialistPayoutAccount);


}
