package com.soupulsar.infrastructure.persistence.mapper;

import com.asaas.apisdk.models.CustomerSaveRequestDto;
import com.asaas.apisdk.models.PaymentSaveRequestBillingType;
import com.asaas.apisdk.models.PaymentSaveRequestDto;
import com.soupulsar.application.dto.request.AsaasCustomerRequest;
import com.soupulsar.application.dto.request.AsaasPaymentRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.lang.String.valueOf;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsaasMapper {

    public static CustomerSaveRequestDto toSdkDtoBuilder(AsaasCustomerRequest request) {
        return CustomerSaveRequestDto.builder()
                .name(request.name())
                .email(request.email())
                .cpfCnpj(request.cpf())
                .phone(request.phone())
                .address(request.address().getStreet())
                .province(request.address().getNeighbourhood())
                .postalCode(request.address().getZipCode())
                .externalReference(request.externalReference())
                .notificationDisabled(false)
                .build();
    }

    public static PaymentSaveRequestDto toSdkDtoBuilder(AsaasPaymentRequest request) {
        return PaymentSaveRequestDto.builder()
                .customer(request.customerId())
                .billingType(PaymentSaveRequestBillingType.fromValue(request.billingType().toString()))
                .value(request.value())
                .dueDate(request.dueDate())
                .description(request.description())
                .externalReference(request.externalReference())
                .build();
    }
}
