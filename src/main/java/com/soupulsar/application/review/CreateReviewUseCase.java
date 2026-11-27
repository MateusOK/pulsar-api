package com.soupulsar.application.review;

import com.soupulsar.application.dto.request.CreateReviewRequest;
import com.soupulsar.application.dto.response.CreateReviewResponse;
import com.soupulsar.application.dto.response.ReviewModerationResult;
import com.soupulsar.application.interfaces.ReviewModerationGateway;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.ReviewModerationUnavailableException;
import com.soupulsar.domain.exceptions.ReviewNotAllowedException;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.review.Review;
import com.soupulsar.domain.repository.ReviewRepository;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CreateReviewUseCase {

    private final SecurityUtils securityUtils;
    private final SpecialistProfileRepository specialistProfileRepository;
    private final SessionRepository sessionRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewContentValidator reviewContentValidator;
    private final ReviewModerationGateway reviewModerationGateway;

    @Transactional
    public CreateReviewResponse execute(CreateReviewRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Review request cannot be null.");
        }

        UUID clientId = securityUtils.getCurrentUserId();

        if (request.specialistId() == null) {
            throw new IllegalArgumentException("Specialist ID cannot be null.");
        }

        specialistProfileRepository.findById(request.specialistId())
                .orElseThrow(() -> new UserNotFoundException("Specialist not found with ID: " + request.specialistId()));

        boolean hasCompletedSession = sessionRepository.existsCompletedSessionByClientIdAndSpecialistId(
                clientId,
                request.specialistId()
        );

        if (!hasCompletedSession) {
            throw new ReviewNotAllowedException("Client must have at least one completed session with the specialist to submit a review.");
        }

        if (reviewRepository.existsPublishedByClientIdAndSpecialistId(clientId, request.specialistId())) {
            throw new ReviewNotAllowedException("Client already has a published review for this specialist.");
        }

        Review review = Review.create(
                request.specialistId(),
                clientId,
                request.comment(),
                request.rating()
        );

        reviewContentValidator.validate(review.getComment());

        ReviewModerationResult moderationResult;
        try {
            moderationResult = reviewModerationGateway.moderate(review.getComment());
        } catch (Exception e) {
            log.error("Moderation failed for review of specialist {} by client {}", request.specialistId(), clientId, e);
            throw new ReviewModerationUnavailableException("Review moderation is currently unavailable. Review was not published.", e);
        }

        if (moderationResult == null) {
            throw new ReviewModerationUnavailableException("Review moderation returned no result. Review was not published.");
        }

        if (moderationResult.flagged()) {
            review.markAsRejected();
        } else {
            review.markAsPublished();
        }

        Review savedReview;
        try {
            savedReview = reviewRepository.save(review);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            if (review.getStatus() == com.soupulsar.domain.model.enums.ReviewStatus.PUBLISHED) {
                throw new ReviewNotAllowedException("Client already has a published review for this specialist.");
            }
            throw e;
        }
        return new CreateReviewResponse(savedReview);
    }
}
