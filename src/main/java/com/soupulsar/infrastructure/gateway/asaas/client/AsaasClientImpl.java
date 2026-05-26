package com.soupulsar.infrastructure.gateway.asaas.client;

import com.asaas.apisdk.AsaasSdk;
import com.asaas.apisdk.models.CustomerSaveRequestDto;
import com.asaas.apisdk.models.PaymentSaveRequestDto;
import com.soupulsar.infrastructure.gateway.asaas.dto.CustomerCreateResponse;
import com.soupulsar.infrastructure.gateway.asaas.dto.PaymentCreateResponse;
import com.soupulsar.infrastructure.gateway.asaas.dto.PaymentDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsaasClientImpl implements AsaasClient {

    private final AsaasSdk asaasSdk;

    @Override
    public CustomerCreateResponse createCustomer(CustomerSaveRequestDto request) {

        var response = asaasSdk.customer.createNewCustomer(request);

        return new CustomerCreateResponse(response.getId());
    }

    @Override
    public PaymentCreateResponse createPayment(PaymentSaveRequestDto request) {

        var response = asaasSdk.payment.createNewPayment(request);

        return new PaymentCreateResponse(response.getId(),  response.getInvoiceUrl());
    }

    @Override
    public PaymentDetailResponse retrieveSinglePayment(String paymentExternalReference) {

        var response = asaasSdk.payment.retrieveASinglePayment(paymentExternalReference);
        return new PaymentDetailResponse(response.getId(), response.getPaymentLink());
    }
}