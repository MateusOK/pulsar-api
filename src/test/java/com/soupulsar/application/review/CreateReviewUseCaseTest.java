package com.soupulsar.application.review;

import com.soupulsar.application.dto.request.CreateReviewRequest;
import com.soupulsar.application.dto.response.CreateReviewResponse;
import com.soupulsar.application.dto.response.ReviewModerationResult;
import com.soupulsar.application.interfaces.ReviewModerationGateway;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.ReviewModerationUnavailableException;
import com.soupulsar.domain.exceptions.ReviewNotAllowedException;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.enums.ReviewStatus;
import com.soupulsar.domain.model.review.Review;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.repository.ReviewRepository;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.test.factory.TestDomainFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.AuthenticationException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CreateReviewUseCaseTest {

    private SecurityUtils securityUtils;
    private SpecialistProfileRepository specialistProfileRepository;
    private SessionRepository sessionRepository;
    private ReviewRepository reviewRepository;
    private ReviewContentValidator reviewContentValidator;
    private ReviewModerationGateway reviewModerationGateway;
    private CreateReviewUseCase useCase;

    private UUID clientId;
    private UUID specialistId;
    private SpecialistProfile specialistProfile;

    @BeforeEach
    void setUp() {
        securityUtils = mock(SecurityUtils.class);
        specialistProfileRepository = mock(SpecialistProfileRepository.class);
        sessionRepository = mock(SessionRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        reviewContentValidator = spy(new ReviewContentValidator());
        reviewModerationGateway = mock(ReviewModerationGateway.class);

        useCase = new CreateReviewUseCase(
                securityUtils,
                specialistProfileRepository,
                sessionRepository,
                reviewRepository,
                reviewContentValidator,
                reviewModerationGateway
        );

        clientId = UUID.randomUUID();
        specialistId = UUID.randomUUID();
        specialistProfile = TestDomainFactory.specialist(specialistId, "wallet-1");

        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldCreateAndPublishReviewWhenUserHasCompletedSessionAndModerationApproves() {
        CreateReviewRequest request = new CreateReviewRequest(
                specialistId,
                new BigDecimal("4.5"),
                "Excelente atendimento, muito atencioso e pontual."
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.of(specialistProfile));
        when(sessionRepository.existsCompletedSessionByClientIdAndSpecialistId(clientId, specialistId)).thenReturn(true);
        when(reviewModerationGateway.moderate(request.comment())).thenReturn(new ReviewModerationResult(false));

        CreateReviewResponse response = useCase.execute(request);

        assertNotNull(response);
        assertNotNull(response.reviewId());
        assertEquals(specialistId, response.specialistId());
        assertEquals(new BigDecimal("4.5"), response.rating());
        assertEquals("Excelente atendimento, muito atencioso e pontual.", response.comment());
        assertEquals(ReviewStatus.PUBLISHED, response.status());

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        Review persistedReview = reviewCaptor.getValue();
        assertEquals(specialistId, persistedReview.getSpecialistId());
        assertEquals(clientId, persistedReview.getClientId());
        assertEquals(ReviewStatus.PUBLISHED, persistedReview.getStatus());

        verify(securityUtils).getCurrentUserId();
        verify(sessionRepository).existsCompletedSessionByClientIdAndSpecialistId(clientId, specialistId);
        verify(reviewContentValidator).validate(request.comment());
        verify(reviewModerationGateway).moderate(request.comment());
    }

    @Test
    void shouldPersistReviewAsRejectedWhenAiModerationFlagsComment() {
        CreateReviewRequest request = new CreateReviewRequest(
                specialistId,
                new BigDecimal("1.0"),
                "Atendimento terrível e cheio de ofensas."
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.of(specialistProfile));
        when(sessionRepository.existsCompletedSessionByClientIdAndSpecialistId(clientId, specialistId)).thenReturn(true);
        when(reviewModerationGateway.moderate(request.comment())).thenReturn(new ReviewModerationResult(true));

        CreateReviewResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(ReviewStatus.REJECTED, response.status());

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        Review persistedReview = reviewCaptor.getValue();
        assertEquals(ReviewStatus.REJECTED, persistedReview.getStatus());
        assertEquals(clientId, persistedReview.getClientId());
        assertEquals(specialistId, persistedReview.getSpecialistId());
    }

    @Test
    void shouldRejectCommentWithSensitiveDataAndNotCallAiModeration() {
        CreateReviewRequest request = new CreateReviewRequest(
                specialistId,
                new BigDecimal("5.0"),
                "Ótimo profissional, meu email é cliente@exemplo.com"
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.of(specialistProfile));
        when(sessionRepository.existsCompletedSessionByClientIdAndSpecialistId(clientId, specialistId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));

        verify(reviewContentValidator).validate(request.comment());
        verifyNoInteractions(reviewModerationGateway);
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void shouldRejectReviewWhenUserHasNoCompletedSessionWithSpecialist() {
        CreateReviewRequest request = new CreateReviewRequest(
                specialistId,
                new BigDecimal("5.0"),
                "Muito bom!"
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.of(specialistProfile));
        when(sessionRepository.existsCompletedSessionByClientIdAndSpecialistId(clientId, specialistId)).thenReturn(false);

        assertThrows(ReviewNotAllowedException.class, () -> useCase.execute(request));

        verify(sessionRepository).existsCompletedSessionByClientIdAndSpecialistId(clientId, specialistId);
        verifyNoInteractions(reviewContentValidator, reviewModerationGateway, reviewRepository);
    }

    @Test
    void shouldIdentifyCurrentlyAuthenticatedUserThroughSecurityUtils() {
        UUID authenticatedClientId = UUID.randomUUID();
        CreateReviewRequest request = new CreateReviewRequest(
                specialistId,
                new BigDecimal("5.0"),
                "Excelente sessão!"
        );

        when(securityUtils.getCurrentUserId()).thenReturn(authenticatedClientId);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.of(specialistProfile));
        when(sessionRepository.existsCompletedSessionByClientIdAndSpecialistId(authenticatedClientId, specialistId)).thenReturn(true);
        when(reviewModerationGateway.moderate(request.comment())).thenReturn(new ReviewModerationResult(false));

        useCase.execute(request);

        verify(securityUtils).getCurrentUserId();
        verify(sessionRepository).existsCompletedSessionByClientIdAndSpecialistId(authenticatedClientId, specialistId);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        assertEquals(authenticatedClientId, reviewCaptor.getValue().getClientId());
    }

    @Test
    void shouldPropagateAuthenticationExceptionWhenNoUserIsAuthenticated() {
        CreateReviewRequest request = new CreateReviewRequest(
                specialistId,
                new BigDecimal("5.0"),
                "Excelente!"
        );

        when(securityUtils.getCurrentUserId()).thenThrow(new AuthenticationException("No authenticated user found") {});

        assertThrows(AuthenticationException.class, () -> useCase.execute(request));
        verifyNoInteractions(specialistProfileRepository, sessionRepository, reviewModerationGateway, reviewRepository);
    }

    @Test
    void shouldThrowNotFoundWhenSpecialistDoesNotExist() {
        CreateReviewRequest request = new CreateReviewRequest(
                specialistId,
                new BigDecimal("5.0"),
                "Excelente!"
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(request));
        verifyNoInteractions(sessionRepository, reviewModerationGateway, reviewRepository);
    }

    @Test
    void shouldNotPublishReviewAndThrowExceptionWhenOpenAiModerationFails() {
        CreateReviewRequest request = new CreateReviewRequest(
                specialistId,
                new BigDecimal("5.0"),
                "Profissional incrível, recomendo muito."
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.of(specialistProfile));
        when(sessionRepository.existsCompletedSessionByClientIdAndSpecialistId(clientId, specialistId)).thenReturn(true);
        when(reviewModerationGateway.moderate(request.comment())).thenThrow(new RuntimeException("OpenAI API timeout"));

        assertThrows(ReviewModerationUnavailableException.class, () -> useCase.execute(request));

        verify(reviewModerationGateway).moderate(request.comment());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void shouldNotPublishReviewAndThrowExceptionWhenModerationReturnsNull() {
        CreateReviewRequest request = new CreateReviewRequest(
                specialistId,
                new BigDecimal("5.0"),
                "Profissional incrível."
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.of(specialistProfile));
        when(sessionRepository.existsCompletedSessionByClientIdAndSpecialistId(clientId, specialistId)).thenReturn(true);
        when(reviewModerationGateway.moderate(request.comment())).thenReturn(null);

        assertThrows(ReviewModerationUnavailableException.class, () -> useCase.execute(request));
        verify(reviewRepository, never()).save(any());
    }
}
