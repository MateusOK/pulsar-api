package com.soupulsar.infrastructure.gateway.asaas;

import com.soupulsar.infrastructure.exceptions.FailedAsaasCustomerException;
import com.soupulsar.infrastructure.gateway.asaas.client.AsaasClient;
import com.soupulsar.infrastructure.gateway.asaas.dto.CustomerCreateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.soupulsar.test.factory.TestDomainFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsaasCustomerGatewayTest {

    @Mock
    AsaasClient asaasClient;

    @InjectMocks
    AsaasCustomerGateway gateway;

    @Test
    void ensureCustomerExists_returns_existing_external_id_and_does_not_call_sdk() {

        var user = TestDomainFactory.simpleUser();
        var client = TestDomainFactory.clientWithExternal(user.getUserId(), "ext-123");

        String result = gateway.ensureCustomerExists(user, client);

        assertEquals("ext-123", result);
        verifyNoInteractions(asaasClient);
    }

    @Test
    void ensureCustomerExists_creates_customer_when_missing() {
        var user = TestDomainFactory.simpleUser();
        var client = TestDomainFactory.client(user.getUserId());

        when(asaasClient.createCustomer(any())).thenReturn(new CustomerCreateResponse("new-id"));

        String result = gateway.ensureCustomerExists(user, client);

        assertEquals("new-id", result);
        verify(asaasClient, times(1)).createCustomer(any());
    }

    @Test
    void ensureCustomerExists_wraps_exceptions() {
        var user = TestDomainFactory.simpleUser();
        var client = TestDomainFactory.client(user.getUserId());

        when(asaasClient.createCustomer(any())).thenThrow(new RuntimeException("sdk failure"));

        assertThrows(FailedAsaasCustomerException.class, () -> gateway.ensureCustomerExists(user, client));
    }
}