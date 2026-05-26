package com.soupulsar.infrastructure.gateway.asaas;

import com.soupulsar.application.dto.response.ExternalPaymentResult;
import com.soupulsar.infrastructure.gateway.asaas.client.AsaasClient;
import com.soupulsar.infrastructure.gateway.asaas.dto.PaymentCreateResponse;
import com.soupulsar.infrastructure.gateway.asaas.dto.PaymentDetailResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.soupulsar.test.factory.TestDomainFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsaasPaymentGatewayTest {

    @Mock
    AsaasClient asaasClient;

    @InjectMocks
    AsaasPaymentGateway gateway;

    @Test
    void processPayment_calls_sdk_and_returns_external_result() {
        var sessionId = UUID.randomUUID();
        var specialistId = UUID.randomUUID();
        var clientId = UUID.randomUUID();

        var payment = TestDomainFactory.simplePayment(sessionId, specialistId, clientId);
        var specialist = TestDomainFactory.specialist(specialistId, "wallet-1");
        var client = TestDomainFactory.clientWithExternal(clientId, "cust-1");
        var session = TestDomainFactory.futureSession(specialistId, clientId);

        when(asaasClient.createPayment(any())).thenReturn(new PaymentCreateResponse("pay-123", "http://invoice"));

        ExternalPaymentResult result = gateway.processPayment(payment, client, specialist, session);

        assertEquals("pay-123", result.externalReference());
        assertEquals("http://invoice", result.paymentUrl());
        verify(asaasClient, times(1)).createPayment(any());
    }

    @Test
    void retrieveSinglePaymentLink_returns_payment_link_from_sdk() {
        when(asaasClient.retrieveSinglePayment("ref-1")).thenReturn(new PaymentDetailResponse("pay-1", "link-1"));

        String link = gateway.retrieveSinglePaymentLink("ref-1");

        assertEquals("link-1", link);
        verify(asaasClient, times(1)).retrieveSinglePayment("ref-1");
    }

    @Test
    void retrieveSinglePaymentLink_wraps_sdk_exception() {
        when(asaasClient.retrieveSinglePayment("ref-2")).thenThrow(new RuntimeException("sdk fail"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> gateway.retrieveSinglePaymentLink("ref-2"));
        assertEquals("sdk fail", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
    }
}
