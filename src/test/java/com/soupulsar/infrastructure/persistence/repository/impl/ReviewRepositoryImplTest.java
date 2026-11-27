package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.enums.ReviewStatus;
import com.soupulsar.domain.model.review.Review;
import com.soupulsar.infrastructure.persistence.entity.review.ReviewEntity;
import com.soupulsar.infrastructure.persistence.repository.ReviewJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReviewRepositoryImplTest {

    private ReviewJpaRepository jpaRepository;
    private ReviewRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(ReviewJpaRepository.class);
        repository = new ReviewRepositoryImpl(jpaRepository);
    }

    @Test
    void shouldPersistReviewWithCorrectStatus() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        Review review = Review.create(specialistId, clientId, "Ótimo atendimento", new BigDecimal("5.0"));
        review.markAsPublished();

        when(jpaRepository.save(any(ReviewEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Review saved = repository.save(review);

        ArgumentCaptor<ReviewEntity> entityCaptor = ArgumentCaptor.forClass(ReviewEntity.class);
        verify(jpaRepository).save(entityCaptor.capture());

        ReviewEntity savedEntity = entityCaptor.getValue();
        assertEquals(review.getReviewId(), savedEntity.getId());
        assertEquals(specialistId, savedEntity.getSpecialistId());
        assertEquals(clientId, savedEntity.getClientId());
        assertEquals(ReviewStatus.PUBLISHED, savedEntity.getStatus());
        assertEquals(ReviewStatus.PUBLISHED, saved.getStatus());
    }

    @Test
    void shouldQueryOnlyPublishedReviewsForSpecialist() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        ReviewEntity publishedEntity = ReviewEntity.builder()
                .id(UUID.randomUUID())
                .specialistId(specialistId)
                .clientId(clientId)
                .comment("Muito bom")
                .rating(new BigDecimal("4.5"))
                .status(ReviewStatus.PUBLISHED)
                .build();

        when(jpaRepository.findBySpecialistIdAndStatusOrderByCreatedAtDesc(specialistId, ReviewStatus.PUBLISHED))
                .thenReturn(List.of(publishedEntity));

        List<Review> result = repository.findPublishedBySpecialistId(specialistId);

        assertEquals(1, result.size());
        assertEquals(ReviewStatus.PUBLISHED, result.getFirst().getStatus());
        verify(jpaRepository).findBySpecialistIdAndStatusOrderByCreatedAtDesc(specialistId, ReviewStatus.PUBLISHED);
    }
}
