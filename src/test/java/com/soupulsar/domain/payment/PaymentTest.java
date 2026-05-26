package com.soupulsar.domain.payment;

import com.soupulsar.domain.model.enums.GatewayPaymentEvent;
import com.soupulsar.domain.model.enums.PaymentMethod;
import com.soupulsar.domain.model.enums.PaymentStatus;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.PaymentAmounts;
import com.soupulsar.domain.model.vo.PaymentSplit;
import com.soupulsar.domain.model.payment.Payment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentTest {

    @Test
    void createWithValidParams() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("100.00")), new Money(new BigDecimal("20.00")));
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("10.00")), new Money(new BigDecimal("70.00")));

        Payment p = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX);

        assertNotNull(p.getId());
        assertEquals(amounts, p.getAmounts());
        assertEquals(split, p.getSplit());
        assertEquals(PaymentStatus.CREATED, p.getPaymentStatus());
        assertTrue(p.isCreated());
    }

    @Test
    void createWithInvalidSplitTotalThrows() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("100.00")), new Money(new BigDecimal("20.00")));
        // split total = 10 + 80 = 90 != final amount 80
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("10.00")), new Money(new BigDecimal("80.00")));

        assertThrows(IllegalArgumentException.class,
                () -> Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX)
        );
    }

    @Test
    void markAsPendingAndPaidFlow() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("50.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("5.00")), new Money(new BigDecimal("45.00")));

        Payment p = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.CREDIT_CARD);

        p.markAsPending("ext-1", "https://pay.link");
        assertEquals(PaymentStatus.PENDING, p.getPaymentStatus());
        assertTrue(p.hasExternalPaymentId());
        assertTrue(p.isPending());

        p.markAsPaid();
        assertEquals(PaymentStatus.PAID, p.getPaymentStatus());
        assertTrue(p.isPaid());
        assertNotNull(p.getPaidAt());
    }

    @Test
    void markAsPendingInvalidStateThrows() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("10.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("1.00")), new Money(new BigDecimal("9.00")));

        Payment p = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.BOLETO);
        p.markAsPending("ext-2", "link");

        assertThrows(IllegalStateException.class, () -> p.markAsPending("ext-3", "other"));
    }

    @Test
    void markAsPaidInvalidStateThrows() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("35.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("3.50")), new Money(new BigDecimal("31.50")));

        Payment p = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.DEBIT_CARD);

        assertThrows(IllegalStateException.class, p::markAsPaid);
    }

    @Test
    void refundWindowBehavior() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("80.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("8.00")), new Money(new BigDecimal("72.00")));

        Payment p = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX);
        p.markAsPending("ext-4", "link");
        p.markAsPaid();

        // session 2 days from now -> within refund window (more than 24h ahead)
        LocalDateTime sessionFar = LocalDateTime.now().plusDays(2);
        p.markAsRefunded(sessionFar);
        assertEquals(PaymentStatus.REFUNDED, p.getPaymentStatus());
        assertNotNull(p.getRefundedAt());

        // create a fresh paid payment and attempt refund outside window
        Payment p2 = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX);
        p2.markAsPending("ext-5", "link");
        p2.markAsPaid();

        LocalDateTime sessionSoon = LocalDateTime.now().plusHours(10);
        assertThrows(IllegalStateException.class, () -> p2.markAsRefunded(sessionSoon));
    }

    @Test
    void handleGatewayEvents() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("60.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("6.00")), new Money(new BigDecimal("54.00")));

        Payment p = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX);
        p.markAsPending("ext-6", "link");

        assertTrue(p.handleGatewayEvent(GatewayPaymentEvent.PAID));
        assertEquals(PaymentStatus.PAID, p.getPaymentStatus());

        // REFUNDED via gateway will attempt to refund using session = now + 1 day which will usually NOT satisfy refund window
        // So it is expected to throw when called on a PAID payment
        assertThrows(IllegalStateException.class, () -> p.handleGatewayEvent(GatewayPaymentEvent.REFUNDED));
    }
}

