package com.soupulsar.infrastructure.gateway.asaas;

import com.asaas.apisdk.AsaasSdk;
import com.asaas.apisdk.models.PaymentSaveRequestBillingType;
import com.asaas.apisdk.models.PaymentSaveRequestDto;
import com.asaas.apisdk.models.PaymentSplitRequestDto;
import com.soupulsar.application.dto.response.ExternalPaymentResult;
import com.soupulsar.application.interfaces.PaymentGateway;
import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsaasPaymentGateway implements PaymentGateway {

    private final AsaasSdk asaasSdk;

    @Override
    public ExternalPaymentResult processPayment(Payment payment, ClientProfile client, SpecialistProfile specialist) {

        try {
            var split = buildPaymentSplitRequest(payment, specialist);
            var paymentRequest = buildPaymentRequest(payment, client, split);
            var paymentResponse = asaasSdk.payment.createNewPayment(paymentRequest);
            return new ExternalPaymentResult(paymentResponse.getId(), paymentResponse.getPaymentLink());
        }
        catch (Exception e) {
            log.error("Failed to process payment in Asaas for payment {}", payment.getId(), e);
            throw new RuntimeException("Failed to process payment in Asaas", e);
        }
    }

    @Override
    public String retrieveSinglePaymentLink(String paymentExternalReference) {
        try {
            var response = asaasSdk.payment.retrieveASinglePayment(paymentExternalReference);
            return response.getPaymentLink();
        } catch (Exception e) {
            log.error("Failed to retrieve payment link from Asaas for payment reference {}", paymentExternalReference, e);
            throw new RuntimeException("Failed to retrieve payment link from Asaas", e);
        }
    }

    private PaymentSaveRequestDto buildPaymentRequest(Payment payment, ClientProfile client, PaymentSplitRequestDto split) {
        return PaymentSaveRequestDto.builder()
                .customer(client.getExternalCustomerId())
                .billingType(PaymentSaveRequestBillingType.fromValue(payment.getPaymentMethod().toString()))
                .value(payment.getAmounts().getFinalAmount().toDouble())
                .externalReference(payment.getId().toString())
                .split(List.of(split))
                .build();
    }

    private PaymentSplitRequestDto buildPaymentSplitRequest(Payment payment, SpecialistProfile specialist) {
        return PaymentSplitRequestDto.builder()
                .walletId(specialist.getExternalPayoutAccountId())
                .fixedValue(payment.getSplit().getSpecialistAmount().toDouble())
                .build();
    }
}