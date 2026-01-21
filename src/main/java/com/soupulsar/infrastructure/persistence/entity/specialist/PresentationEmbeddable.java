package com.soupulsar.infrastructure.persistence.entity.specialist;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public final class PresentationEmbeddable {

    private String about;
    private String personalDescription;
    private String presentationVideoUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String base64Image;

    @ElementCollection
    @CollectionTable(name = "academicHistory", joinColumns = @JoinColumn(name = "specialist_id"))
    @Column(name = "academic_history")
    private List<String> academicHistory = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "specializations", joinColumns = @JoinColumn(name = "specialist_id"))
    @Column(name = "specialization")
    private List<String> specializations = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "therapeutic_approaches", joinColumns = @JoinColumn(name = "specialist_id"))
    @Column(name = "therapeutic_approach")
    private List<String> therapeuticApproaches = new ArrayList<>();
}