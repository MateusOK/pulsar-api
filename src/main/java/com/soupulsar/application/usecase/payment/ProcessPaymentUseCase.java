package com.soupulsar.application.usecase.payment;

import com.soupulsar.application.dto.response.ExternalPaymentResult;
import com.soupulsar.application.dto.response.PaymentProcessedResponse;
import com.soupulsar.application.interfaces.CustomerGateway;
import com.soupulsar.application.interfaces.PaymentGateway;
import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.ClientProfileRepository;
import com.soupulsar.domain.repository.PaymentRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final SpecialistProfileRepository specialistProfileRepository;
    private final UserRepository userRepository;
    private final CustomerGateway customerGateway;
    private final PaymentGateway paymentGateway;

    @Transactional
    public PaymentProcessedResponse execute(UUID paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

        if (payment.isPending() && payment.hasExternalPaymentId()){
            log.info("Payment {} is already processed externally. Returning existing link.", paymentId);
            return new PaymentProcessedResponse(paymentId, payment.getPaymentLink());
        }

        if (!payment.isCreated()){
            throw new IllegalStateException("Payment is in invalid state for processing: " + payment.getPaymentStatus());
        }

        User user = userRepository.findById(payment.getClientId())
                .orElseThrow(() -> new RuntimeException("User not found: " + payment.getClientId()));

        ClientProfile clientProfile = clientProfileRepository.findById(payment.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found: " + payment.getClientId()));

        SpecialistProfile specialistProfile = specialistProfileRepository.findById(payment.getSpecialistId())
                .orElseThrow(() -> new RuntimeException("Specialist not found: " + payment.getSpecialistId()));

        if (!specialistProfile.canReceivePayments()){
            log.error("Specialist {} cannot receive payments. Aborting payment processing.", specialistProfile.getUserId());
            throw new IllegalStateException("Specialist cannot receive payments");
        }

        if (!clientProfile.hasExternalCustomerId()){
            log.info("Client {} does not have an external customer ID. Creating customer in payment gateway.", clientProfile.getUserId());
            String customerId = customerGateway.ensureCustomerExists(user, clientProfile);
            clientProfile.attachExternalCustomerId(customerId);
            clientProfileRepository.save(clientProfile);
        }

        ExternalPaymentResult externalResult = paymentGateway.processPayment(payment, clientProfile, specialistProfile);
        payment.markAsPending(externalResult.externalReference(), externalResult.paymentUrl());
        paymentRepository.save(payment);

        log.info("Payment {} processed successfully. ExternalId: {}", paymentId, externalResult.externalReference());

        return new PaymentProcessedResponse(paymentId, externalResult.paymentUrl());
    }
}