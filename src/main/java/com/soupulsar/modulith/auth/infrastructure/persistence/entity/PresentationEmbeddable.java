package com.soupulsar.modulith.auth.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}