package com.soupulsar.modulith.auth.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public final class AddressEmbeddable {

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String neighbourhood;

}
