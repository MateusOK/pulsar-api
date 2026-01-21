package com.soupulsar.domain.model.vo;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Presentation {

    String about;
    String personalDescription;
    String presentationVideoUrl;
    String base64Image;
    List<String> academicHistory;
    List<String> specializations;
    List<String> therapeuticApproaches;
}