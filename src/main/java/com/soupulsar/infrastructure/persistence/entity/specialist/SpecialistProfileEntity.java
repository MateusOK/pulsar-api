package com.soupulsar.infrastructure.persistence.entity.specialist;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "specialist_profiles")
@Getter
@Setter
public class SpecialistProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID profileId;
    private UUID userId;
    private String registrationNumber;

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