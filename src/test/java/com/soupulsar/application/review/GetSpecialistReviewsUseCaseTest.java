package com.soupulsar.application.review;

import com.soupulsar.application.dto.response.SpecialistReviewResponse;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.enums.ReviewStatus;
import com.soupulsar.domain.model.review.Review;
import com.soupulsar.domain.repository.ReviewRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.test.factory.TestDomainFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class GetSpecialistReviewsUseCaseTest {

    private SpecialistProfileRepository specialistProfileRepository;
    private ReviewRepository reviewRepository;
    private GetSpecialistReviewsUseCase useCase;

    @BeforeEach
    void setUp() {
        specialistProfileRepository = mock(SpecialistProfileRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        useCase = new GetSpecialistReviewsUseCase(specialistProfileRepository, reviewRepository);
    }

    @Test
    void shouldReturnOnlyPublishedReviewsForSpecialist() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        Review publishedReview = Review.restore(
                UUID.randomUUID(), specialistId, clientId,
                "Ótimo atendimento!", new BigDecimal("5.0"), ReviewStatus.PUBLISHED
        );
        Review rejectedReview = Review.restore(
                UUID.randomUUID(), specialistId, clientId,
                "Conteúdo impróprio", new BigDecimal("1.0"), ReviewStatus.REJECTED
        );
        Review pendingReview = Review.restore(
                UUID.randomUUID(), specialistId, clientId,
                "Aguardando moderação", new BigDecimal("4.0"), ReviewStatus.PENDING
        );

        when(specialistProfileRepository.findById(specialistId))
                .thenReturn(Optional.of(TestDomainFactory.specialist(specialistId, "wallet-1")));
        when(reviewRepository.findPublishedBySpecialistId(specialistId))
                .thenReturn(List.of(publishedReview, rejectedReview, pendingReview));

        List<SpecialistReviewResponse> responses = useCase.execute(specialistId);

        assertEquals(1, responses.size());
        assertEquals(publishedReview.getReviewId(), responses.getFirst().reviewId());
        assertEquals("Ótimo atendimento!", responses.getFirst().comment());
        verify(reviewRepository).findPublishedBySpecialistId(specialistId);
    }

    @Test
    void shouldThrowWhenSpecialistDoesNotExist() {
        UUID specialistId = UUID.randomUUID();
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(specialistId));
        verifyNoInteractions(reviewRepository);
    }
}
