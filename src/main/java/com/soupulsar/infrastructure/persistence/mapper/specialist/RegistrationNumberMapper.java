package com.soupulsar.infrastructure.persistence.mapper.specialist;

import com.soupulsar.domain.model.vo.RegistrationNumber;
import com.soupulsar.infrastructure.persistence.entity.specialist.RegistrationNumberEmbeddable;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RegistrationNumberMapper {

    public static RegistrationNumberEmbeddable toEmbeddable(RegistrationNumber registrationNumber) {
        if (registrationNumber == null) return null;

        return new RegistrationNumberEmbeddable(registrationNumber.value());
    }

    public static RegistrationNumber toValueObject(RegistrationNumberEmbeddable registrationNumberEmbeddable) {
        if (registrationNumberEmbeddable == null) return null;

        return new RegistrationNumber(registrationNumberEmbeddable.getValue());
    }
}