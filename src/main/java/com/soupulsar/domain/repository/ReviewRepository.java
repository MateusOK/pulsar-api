package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.review.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository {

    Review save(Review review);

    List<Review> findPublishedBySpecialistId(UUID specialistId);
    boolean existsPublishedByClientIdAndSpecialistId(UUID clientId, UUID specialistId);
    java.util.Map<UUID, java.math.BigDecimal> averagePublishedRatingBySpecialistIds(List<UUID> specialistIds);
}
