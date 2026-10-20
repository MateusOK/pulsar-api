package com.soupulsar.application.partner;

import com.soupulsar.domain.model.partner.PartnerEligibilityType;
import com.soupulsar.domain.repository.PartnerEligibilityRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreatePartnerEligibilityUseCaseTest {

    @Test
    void createsAndPersistsEligibility() {
        PartnerEligibilityRepository repository = mock(PartnerEligibilityRepository.class);
        CreatePartnerEligibilityUseCase useCase = new CreatePartnerEligibilityUseCase(repository);
        UUID discountId = UUID.randomUUID();

        useCase.execute(new CreatePartnerEligibilityRequest(discountId, PartnerEligibilityType.DOMAIN, "example.com"));

        verify(repository).save(argThat(eligibility -> {
            assertNotNull(eligibility.getId());
            assertEquals(discountId, eligibility.getPartnerDiscountId());
            assertEquals(PartnerEligibilityType.DOMAIN, eligibility.getType());
            assertEquals("example.com", eligibility.getValue());
            return true;
        }));
    }
}
