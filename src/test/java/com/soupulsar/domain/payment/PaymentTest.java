package com.soupulsar.domain.payment;

import com.soupulsar.domain.model.enums.PaymentMethod;
import com.soupulsar.domain.model.enums.PaymentStatus;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.PaymentAmounts;
import com.soupulsar.domain.model.vo.PaymentSplit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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

        Payment payment = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX);

        assertNotNull(payment.getId());
        assertEquals(amounts, payment.getAmounts());
        assertEquals(split, payment.getSplit());
        assertEquals(PaymentStatus.CREATED, payment.getPaymentStatus());
        assertTrue(payment.isCreated());
    }

    @Test
    void createWithInvalidSplitTotalThrows() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("100.00")), new Money(new BigDecimal("20.00")));
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("10.00")), new Money(new BigDecimal("80.00")));

        assertThrows(IllegalArgumentException.class,
                () -> Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX)
        );
    }

    @Test
    void markAsPendingAndPaidFlow() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("50.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("5.00")), new Money(new BigDecimal("45.00")));

        Payment payment = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.CREDIT_CARD);

        payment.markAsPending("ext-1", "https://pay.link");
        assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());
        assertTrue(payment.hasExternalPaymentId());
        assertTrue(payment.isPending());

        payment.markAsPaid();
        assertEquals(PaymentStatus.PAID, payment.getPaymentStatus());
        assertTrue(payment.isPaid());
        assertNotNull(payment.getPaidAt());
    }

    @Test
    void markAsPendingInvalidStateThrows() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("10.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("1.00")), new Money(new BigDecimal("9.00")));

        Payment payment = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.BOLETO);
        payment.markAsPending("ext-2", "link");

        assertThrows(IllegalStateException.class, () -> payment.markAsPending("ext-3", "other"));
    }

    @Test
    void markAsPaidInvalidStateThrows() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("35.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("3.50")), new Money(new BigDecimal("31.50")));

        Payment payment = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.DEBIT_CARD);

        assertThrows(IllegalStateException.class, payment::markAsPaid);
    }

    @Test
    void requestRefundFlow() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("80.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("8.00")), new Money(new BigDecimal("72.00")));

        Payment payment = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX);
        payment.markAsPending("ext-4", "link");
        payment.markAsPaid();

        payment.requestRefund();

        assertEquals(PaymentStatus.REFUND_PENDING, payment.getPaymentStatus());
    }

    @Test
    void requestRefundInvalidStateThrows() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("60.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("6.00")), new Money(new BigDecimal("54.00")));

        Payment payment = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX);
        payment.markAsPending("ext-5", "link");

        assertThrows(IllegalStateException.class, payment::requestRefund);
    }

    @Test
    void markAsRefundedFlow() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("90.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("9.00")), new Money(new BigDecimal("81.00")));

        Payment payment = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX);
        payment.markAsPending("ext-6", "link");
        payment.markAsPaid();
        payment.requestRefund();

        payment.markAsRefunded();

        assertEquals(PaymentStatus.REFUNDED, payment.getPaymentStatus());
        assertNotNull(payment.getRefundedAt());
    }

    @Test
    void markAsRefundedInvalidStateThrows() {
        PaymentAmounts amounts = new PaymentAmounts(new Money(new BigDecimal("45.00")), Money.zero());
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("4.50")), new Money(new BigDecimal("40.50")));

        Payment payment = Payment.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), amounts, split, PaymentMethod.PIX);
        payment.markAsPending("ext-7", "link");
        payment.markAsPaid();

        assertThrows(IllegalStateException.class, payment::markAsRefunded);
    }
}

