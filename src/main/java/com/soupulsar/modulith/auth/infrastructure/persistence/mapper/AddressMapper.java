package com.soupulsar.modulith.auth.infrastructure.persistence.mapper;

import com.soupulsar.modulith.auth.domain.model.vo.Address;
import com.soupulsar.modulith.auth.infrastructure.persistence.entity.AddressEmbeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressMapper {

    public static AddressEmbeddable toEmbeddable(Address address) {
            if (address == null) return null;

            return AddressEmbeddable.builder()
                    .street(address.getStreet())
                    .city(address.getCity())
                    .state(address.getState())
                    .zipCode(address.getZipCode())
                    .neighbourhood(address.getNeighbourhood())
                    .build();
    }

    public static Address toValueObject(AddressEmbeddable embeddable) {
        if (embeddable == null) return null;

        return Address.builder()
                .street(embeddable.getStreet())
                .city(embeddable.getCity())
                .state(embeddable.getState())
                .zipCode(embeddable.getZipCode())
                .neighbourhood(embeddable.getNeighbourhood())
                .build();
    }

}