package com.soupulsar.domain.payment;

import com.soupulsar.domain.model.enums.PaymentProvider;
import com.soupulsar.domain.model.payment.SpecialistPayoutAccount;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecialistPayoutAccountTest {

    @Test
    void createSuccessAndDeactivate() {
        UUID specialistId = UUID.randomUUID();
        SpecialistPayoutAccount account = SpecialistPayoutAccount.create(specialistId, PaymentProvider.ASAAS, "ext-acc-1");

        assertNotNull(account.getId());
        assertEquals(specialistId, account.getSpecialistId());
        assertEquals(PaymentProvider.ASAAS, account.getProvider());
        assertTrue(account.isActive());

        account.deactivate();
        assertFalse(account.isActive());
    }

    @Test
    void createInvalidParametersThrow() {
        UUID specialistId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> SpecialistPayoutAccount.create(null, PaymentProvider.ASAAS, "ext"));
        assertThrows(IllegalArgumentException.class, () -> SpecialistPayoutAccount.create(specialistId, null, "ext"));
        assertThrows(IllegalArgumentException.class, () -> SpecialistPayoutAccount.create(specialistId, PaymentProvider.ASAAS, " "));
    }
}

