package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.review.Review;

import java.math.BigDecimal;
import java.util.UUID;

public record SpecialistReviewResponse(
        UUID reviewId,
        UUID specialistId,
        BigDecimal rating,
        String comment
) {
    public SpecialistReviewResponse(Review review) {
        this(
                review.getReviewId(),
                review.getSpecialistId(),
                review.getRating(),
                review.getComment()
        );
    }
}
