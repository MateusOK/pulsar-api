package com.soupulsar.application.interfaces;

import com.soupulsar.application.dto.response.ReviewModerationResult;

public interface ReviewModerationGateway {

    ReviewModerationResult moderate(String comment);
}