package com.soupulsar.application.interfaces;

import com.soupulsar.application.dto.response.ExternalPaymentResult;
import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.specialist.SpecialistProfile;

public interface PaymentGateway {
    ExternalPaymentResult processPayment(Payment payment, ClientProfile customer, SpecialistProfile specialist);
    String retrieveSinglePaymentLink(String paymentExternalReference);
}