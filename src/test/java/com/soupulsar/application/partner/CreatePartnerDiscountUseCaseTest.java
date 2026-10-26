package com.soupulsar.application.partner;

import com.soupulsar.domain.repository.PartnerDiscountRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreatePartnerDiscountUseCaseTest {

    @Test
    void createsAndPersistsDiscountFromClockTime() {
        Instant now = Instant.now();
        Clock clock = Clock.fixed(now, Clock.systemUTC().getZone());
        PartnerDiscountRepository repository = mock(PartnerDiscountRepository.class);
        CreatePartnerDiscountUseCase useCase = new CreatePartnerDiscountUseCase(repository, clock);
        UUID partnerId = UUID.randomUUID();
        Instant expiresAt = now.plus(Duration.ofDays(30));

        useCase.execute(new CreatePartnerDiscountRequest(partnerId, new BigDecimal("15"), expiresAt));

        verify(repository).save(argThat(discount -> {
            assertNotNull(discount.getId());
            assertEquals(partnerId, discount.getPartnerId());
            assertEquals(new BigDecimal("15.00"), discount.getPercentage().value());
            assertEquals(expiresAt, discount.getExpiresAt());
            return discount.isApplicableAt(now);
        }));
    }
}
