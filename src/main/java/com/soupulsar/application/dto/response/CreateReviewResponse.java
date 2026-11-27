package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.enums.ReviewStatus;
import com.soupulsar.domain.model.review.Review;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateReviewResponse(
        UUID reviewId,
        UUID specialistId,
        BigDecimal rating,
        String comment,
        ReviewStatus status
) {
    public CreateReviewResponse(Review review) {
        this(
                review.getReviewId(),
                review.getSpecialistId(),
                review.getRating(),
                review.getComment(),
                review.getStatus()
        );
    }
}
