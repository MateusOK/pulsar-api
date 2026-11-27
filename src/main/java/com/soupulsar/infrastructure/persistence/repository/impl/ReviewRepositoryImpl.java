package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.enums.ReviewStatus;
import com.soupulsar.domain.model.review.Review;
import com.soupulsar.domain.repository.ReviewRepository;
import com.soupulsar.infrastructure.persistence.entity.review.ReviewEntity;
import com.soupulsar.infrastructure.persistence.mapper.review.ReviewMapper;
import com.soupulsar.infrastructure.persistence.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository jpaRepository;

    @Override
    public Review save(Review review) {
        ReviewEntity entity = ReviewMapper.toEntity(review);
        ReviewEntity saved = jpaRepository.save(entity);
        return ReviewMapper.toModel(saved);
    }

    @Override
    public List<Review> findPublishedBySpecialistId(UUID specialistId) {
        if (specialistId == null) {
            return Collections.emptyList();
        }
        return jpaRepository.findBySpecialistIdAndStatusOrderByCreatedAtDesc(specialistId, ReviewStatus.PUBLISHED)
                .stream()
                .map(ReviewMapper::toModel)
                .toList();
    }

    @Override public boolean existsPublishedByClientIdAndSpecialistId(UUID clientId, UUID specialistId) {
        return jpaRepository.existsByClientIdAndSpecialistIdAndStatus(clientId, specialistId, ReviewStatus.PUBLISHED);
    }

    @Override public java.util.Map<UUID, java.math.BigDecimal> averagePublishedRatingBySpecialistIds(List<UUID> ids) {
        if (ids.isEmpty()) return java.util.Map.of();
        return jpaRepository.averageRatings(ids, ReviewStatus.PUBLISHED).stream().collect(java.util.stream.Collectors.toMap(
                r -> (UUID) r[0],
                r -> r[1] instanceof java.math.BigDecimal value
                        ? value
                        : java.math.BigDecimal.valueOf(((Number) r[1]).doubleValue())
        ));
    }
}
