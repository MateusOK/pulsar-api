package com.soupulsar.infrastructure.gateway.asaas;

import com.asaas.apisdk.AsaasSdk;
import com.asaas.apisdk.models.PaymentSaveRequestBillingType;
import com.asaas.apisdk.models.PaymentSaveRequestDto;
import com.asaas.apisdk.models.PaymentSplitRequestDto;
import com.soupulsar.application.dto.response.ExternalPaymentResult;
import com.soupulsar.application.interfaces.PaymentGateway;
import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.infrastructure.gateway.asaas.client.AsaasClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsaasPaymentGateway implements PaymentGateway {

    private final AsaasClient asaasSdk;

    @Override
    public ExternalPaymentResult processPayment(Payment payment, ClientProfile client, SpecialistProfile specialist, Session session) {

        try {
            var split = buildPaymentSplitRequest(payment, specialist);
            var paymentRequest = buildPaymentRequest(payment, client, split, session);
            var paymentResponse = asaasSdk.createPayment(paymentRequest);
            return new ExternalPaymentResult(paymentResponse.id(), paymentResponse.invoiceUrl());
        }
        catch (Exception e) {
            log.error("Failed to process payment in Asaas for payment {}", payment.getId(), e);
            throw new RuntimeException("Failed to process payment in Asaas", e);
        }
    }

    @Override
    public String retrieveSinglePaymentLink(String paymentExternalReference) {
        try {
            var response = asaasSdk.retrieveSinglePayment(paymentExternalReference);
            return response.paymentLink();
        } catch (Exception e) {
            log.error("Failed to retrieve payment link from Asaas for payment reference {}", paymentExternalReference, e);
            throw new RuntimeException("Failed to retrieve payment link from Asaas", e);
        }
    }

    private PaymentSaveRequestDto buildPaymentRequest(Payment payment, ClientProfile client, PaymentSplitRequestDto split, Session session) {

        LocalDate dueDate = session.getStartAt().minusHours(24).toLocalDate();
        if (dueDate.isBefore(LocalDate.now())) {
            dueDate = LocalDate.now().plusDays(1);
        }
        return PaymentSaveRequestDto.builder()
                .customer(client.getExternalCustomerId())
                .billingType(PaymentSaveRequestBillingType.fromValue(payment.getPaymentMethod().toString()))
                .value(payment.getAmounts().getFinalAmount().toDouble())
                .externalReference(payment.getId().toString())
                .split(List.of(split))
                .dueDate(dueDate.toString())
                .build();
    }

    private PaymentSplitRequestDto buildPaymentSplitRequest(Payment payment, SpecialistProfile specialist) {
        return PaymentSplitRequestDto.builder()
                .walletId(specialist.getExternalPayoutAccountId())
                .fixedValue(payment.getSplit().getSpecialistAmount().toDouble())
                .build();
    }
}