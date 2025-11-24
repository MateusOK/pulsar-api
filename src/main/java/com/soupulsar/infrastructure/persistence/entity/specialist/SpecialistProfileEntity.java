package com.soupulsar.infrastructure.persistence.entity.specialist;

import com.soupulsar.domain.model.enums.SpecialistType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "specialist_profiles")
@Getter
@Setter
public class SpecialistProfileEntity {

    @Id
    private UUID userId;

    private String registrationNumber;
    private BigDecimal sessionPrice;

    @Enumerated(EnumType.STRING)
    private SpecialistType specialistType;

    @Embedded
    private PresentationEmbeddable presentation;

    @ElementCollection
    @CollectionTable(name = "specialist_formation", joinColumns = @JoinColumn(name = "specialist_id"))
    @Column(name = "formation")
    private List<String> formations = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "specialist_specialties", joinColumns = @JoinColumn(name = "specialist_id"))
    @Column(name = "specialty")
    private List<String> specialties = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "specialist_approaches", joinColumns = @JoinColumn(name = "specialist_id"))
    @Column(name = "approach")
    private List<String> approaches = new ArrayList<>();
}