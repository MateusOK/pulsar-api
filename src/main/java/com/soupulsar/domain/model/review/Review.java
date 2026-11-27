package com.soupulsar.domain.model.review;

import com.soupulsar.domain.model.enums.ReviewStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review {

    private final UUID reviewId;
    private final UUID specialistId;
    private final UUID clientId;

    private String comment;
    private BigDecimal rating;
    private ReviewStatus status;

    public static Review create(UUID specialistId, UUID clientId, String comment, BigDecimal rating) {
        if (specialistId == null) {
            throw new IllegalArgumentException("Specialist ID cannot be null.");
        }
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID cannot be null.");
        }
        validateComment(comment);
        validateRating(rating);
        return new ReviewBuilder()
                .reviewId(UUID.randomUUID())
                .specialistId(specialistId)
                .clientId(clientId)
                .comment(comment.trim())
                .rating(rating)
                .status(ReviewStatus.PENDING)
                .build();
    }

    public static Review restore(UUID reviewId, UUID specialistId, UUID clientId, String comment, BigDecimal rating, ReviewStatus status) {
        return new Review(reviewId,specialistId, clientId, comment, rating, status);
    }

    public void markAsPublished() {
        if (this.status != ReviewStatus.PENDING) {
            throw new IllegalStateException("Only pending reviews can be marked as published.");
        }
        this.status = ReviewStatus.PUBLISHED;
    }

    public void markAsRejected() {
        if (this.status != ReviewStatus.PENDING) {
            throw new IllegalStateException("Only pending reviews can be marked as rejected.");
        }
        this.status = ReviewStatus.REJECTED;
    }

    private static void validateComment(String comment) {
        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException("Review comment cannot be empty.");
        }
        if (comment.trim().length() > 280) {
            throw new IllegalArgumentException("Review comment cannot exceed 280 characters.");
        }
    }

    private static void validateRating(BigDecimal rating) {
        if (rating == null || rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
    }
}