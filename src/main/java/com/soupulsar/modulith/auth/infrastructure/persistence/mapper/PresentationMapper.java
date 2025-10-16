package com.soupulsar.modulith.auth.infrastructure.persistence.mapper;

import com.soupulsar.modulith.auth.domain.model.vo.Presentation;
import com.soupulsar.modulith.auth.infrastructure.persistence.entity.PresentationEmbeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PresentationMapper {

    public static PresentationEmbeddable toEmbeddable(Presentation presentation) {
        if (presentation == null) return null;


        return PresentationEmbeddable.builder()
                .personalDescription(presentation.getPersonalDescription())
                .about(presentation.getAbout())
                .presentationVideoUrl(presentation.getPresentationVideoUrl())
                .base64Image(presentation.getBase64Image())
                .build();
    }

    public static Presentation toValueObject(PresentationEmbeddable embeddable) {
        if (embeddable == null) return null;

        return Presentation.builder()
                .personalDescription(embeddable.getPersonalDescription())
                .about(embeddable.getAbout())
                .presentationVideoUrl(embeddable.getPresentationVideoUrl())
                .base64Image(embeddable.getBase64Image())
                .build();
    }
}
