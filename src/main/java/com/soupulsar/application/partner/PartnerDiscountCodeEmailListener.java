package com.soupulsar.application.partner;

import com.soupulsar.application.interfaces.EmailGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PartnerDiscountCodeEmailListener {

    private final EmailGateway emailGateway;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PartnerDiscountCodeIssuedEvent event) {
        emailGateway.sendPartnerDiscountCodeEmail(event.rawCode(), event.email(), event.expiresAt());
    }
}