package com.soupulsar.infrastructure.persistence.entity.specialist;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.infrastructure.persistence.entity.payment.MoneyEmbeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "specialist_profiles")
@Getter
@Setter
public class SpecialistProfileEntity {

    @Id
    private UUID userId;

    private String externalPayoutAccountId;

    @Embedded
    private RegistrationNumberEmbeddable registrationNumber;
    @Embedded
    private MoneyEmbeddable sessionPrice;

    @Enumerated(EnumType.STRING)
    private SpecialistType specialistType;

    @Embedded
    private PresentationEmbeddable presentation;
}