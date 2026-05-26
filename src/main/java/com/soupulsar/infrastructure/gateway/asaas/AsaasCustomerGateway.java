package com.soupulsar.infrastructure.gateway.asaas;

import com.asaas.apisdk.AsaasSdk;
import com.asaas.apisdk.models.CustomerSaveRequestDto;
import com.soupulsar.application.interfaces.CustomerGateway;
import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.infrastructure.exceptions.FailedAsaasCustomerException;
import com.soupulsar.infrastructure.gateway.asaas.client.AsaasClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsaasCustomerGateway implements CustomerGateway {

    private final AsaasClient asaasSdk;

    @Override
    public String ensureCustomerExists(User user, ClientProfile client) {

        try {
            if (client.hasExternalCustomerId()) {
                return client.getExternalCustomerId();
            }

            var request = buildCustomerRequest(user);
            var response = asaasSdk.createCustomer(request);
            return response.id();

        } catch (Exception e) {
            log.error("Failed to ensure customer exists in Asaas for user {}", user.getUserId(), e);
            throw new FailedAsaasCustomerException("Failed to ensure customer exists in Asaas", e);
        }
    }

    private CustomerSaveRequestDto buildCustomerRequest(User user) {
        return CustomerSaveRequestDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .cpfCnpj(user.getCpf())
                .phone(user.getTelephone())
                .province(user.getAddress().getNeighbourhood())
                .postalCode(user.getAddress().getZipCode())
                .externalReference(user.getUserId().toString())
                .build();
    }
}