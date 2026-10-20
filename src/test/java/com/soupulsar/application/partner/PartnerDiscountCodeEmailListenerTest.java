package com.soupulsar.application.partner;

import com.soupulsar.application.interfaces.EmailGateway;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PartnerDiscountCodeEmailListenerTest {

    @Test
    void sendsIssuedCodeToRecipient() {
        EmailGateway emailGateway = mock(EmailGateway.class);
        PartnerDiscountCodeEmailListener listener = new PartnerDiscountCodeEmailListener(emailGateway);
        Instant expiresAt = Instant.now().plusSeconds(900);
        PartnerDiscountCodeIssuedEvent event =
                new PartnerDiscountCodeIssuedEvent("raw-code", "user@example.com", expiresAt);

        listener.handle(event);

        verify(emailGateway).sendPartnerDiscountCodeEmail("raw-code", "user@example.com", expiresAt);
    }
}
