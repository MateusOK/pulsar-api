package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.vo.Address;
import lombok.Builder;

@Builder
public record AddressResponse(

        String street,
        String city,
        String state,
        String zipcode,
        String neighbourhood

) {

    public static AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipcode(address.getZipCode())
                .neighbourhood(address.getNeighbourhood())
                .build();
    }
}