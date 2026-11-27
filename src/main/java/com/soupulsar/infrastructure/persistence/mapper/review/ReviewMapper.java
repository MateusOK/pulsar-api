package com.soupulsar.infrastructure.persistence.mapper.review;

import com.soupulsar.domain.model.review.Review;
import com.soupulsar.infrastructure.persistence.entity.review.ReviewEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewMapper {

    public static ReviewEntity toEntity(Review review) {
        if (review == null) return null;
        return ReviewEntity.builder()
                .id(review.getReviewId())
                .specialistId(review.getSpecialistId())
                .clientId(review.getClientId())
                .comment(review.getComment())
                .rating(review.getRating())
                .status(review.getStatus())
                .build();
    }

    public  static Review toModel(ReviewEntity entity) {
        if (entity == null) return null;
        return Review.restore(
                entity.getId(),
                entity.getSpecialistId(),
                entity.getClientId(),
                entity.getComment(),
                entity.getRating(),
                entity.getStatus()
        );
    }
}