package com.soupulsar.infrastructure.gateway.asaas.client;

import com.asaas.apisdk.models.CustomerSaveRequestDto;
import com.asaas.apisdk.models.PaymentSaveRequestDto;
import com.soupulsar.infrastructure.gateway.asaas.dto.CustomerCreateResponse;
import com.soupulsar.infrastructure.gateway.asaas.dto.PaymentCreateResponse;
import com.soupulsar.infrastructure.gateway.asaas.dto.PaymentDetailResponse;

public interface AsaasClient {

    CustomerCreateResponse createCustomer(CustomerSaveRequestDto request);
    PaymentCreateResponse  createPayment(PaymentSaveRequestDto request);
    PaymentDetailResponse retrieveSinglePayment(String paymentExternalReference);

}