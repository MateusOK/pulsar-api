package com.soupulsar.modulith.auth.domain.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Address {

    String street;
    String city;
    String state;
    String zipCode;
    String neighbourhood;

    public Address withZipCode(String newZipCode) {
        return Address.builder()
                .street(this.street)
                .city(this.city)
                .state(this.state)
                .zipCode(newZipCode)
                .neighbourhood(this.neighbourhood)
                .build();
    }
}