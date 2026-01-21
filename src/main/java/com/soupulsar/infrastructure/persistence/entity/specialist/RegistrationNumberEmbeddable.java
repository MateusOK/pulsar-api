package com.soupulsar.infrastructure.persistence.entity.specialist;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class RegistrationNumberEmbeddable {

    @Column(name = "registration_number")
    private String value;
}