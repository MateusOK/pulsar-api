package com.soupulsar.application.review;

import com.soupulsar.application.dto.response.SpecialistReviewResponse;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.enums.ReviewStatus;
import com.soupulsar.domain.repository.ReviewRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetSpecialistReviewsUseCase {

    private final SpecialistProfileRepository specialistProfileRepository;
    private final ReviewRepository reviewRepository;

    public List<SpecialistReviewResponse> execute(UUID specialistId) {
        if (specialistId == null) {
            throw new IllegalArgumentException("Specialist ID cannot be null.");
        }

        specialistProfileRepository.findById(specialistId)
                .orElseThrow(() -> new UserNotFoundException("Specialist not found with ID: " + specialistId));

        return reviewRepository.findPublishedBySpecialistId(specialistId)
                .stream()
                .filter(review -> review.getStatus() == ReviewStatus.PUBLISHED)
                .map(SpecialistReviewResponse::new)
                .toList();
    }
}
