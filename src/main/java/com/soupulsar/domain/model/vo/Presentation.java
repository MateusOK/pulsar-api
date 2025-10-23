package com.soupulsar.domain.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Presentation {

    String about;
    String personalDescription;
    String presentationVideoUrl;
    String base64Image;
}