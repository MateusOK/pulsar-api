package com.soupulsar.domain.review;

import com.soupulsar.domain.model.enums.ReviewStatus;
import com.soupulsar.domain.model.review.Review;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewTest {

    @Test
    void shouldCreatePendingReviewAndTransitionToPublished() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        Review review = Review.create(specialistId, clientId, "  Excelente consulta  ", new BigDecimal("5.0"));

        assertNotNull(review.getReviewId());
        assertEquals(specialistId, review.getSpecialistId());
        assertEquals(clientId, review.getClientId());
        assertEquals("Excelente consulta", review.getComment());
        assertEquals(ReviewStatus.PENDING, review.getStatus());

        review.markAsPublished();
        assertEquals(ReviewStatus.PUBLISHED, review.getStatus());
    }

    @Test
    void shouldTransitionFromPendingToRejected() {
        Review review = Review.create(UUID.randomUUID(), UUID.randomUUID(), "Comentário", new BigDecimal("2.0"));

        review.markAsRejected();
        assertEquals(ReviewStatus.REJECTED, review.getStatus());
        assertThrows(IllegalStateException.class, review::markAsPublished);
    }

    @Test
    void shouldRejectInvalidInputs() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> Review.create(null, clientId, "Bom", new BigDecimal("4.0")));
        assertThrows(IllegalArgumentException.class, () -> Review.create(specialistId, null, "Bom", new BigDecimal("4.0")));
        assertThrows(IllegalArgumentException.class, () -> Review.create(specialistId, clientId, "   ", new BigDecimal("4.0")));
        assertThrows(IllegalArgumentException.class, () -> Review.create(specialistId, clientId, "a".repeat(281), new BigDecimal("4.0")));
        assertThrows(IllegalArgumentException.class, () -> Review.create(specialistId, clientId, "Bom", new BigDecimal("-0.1")));
        assertThrows(IllegalArgumentException.class, () -> Review.create(specialistId, clientId, "Bom", new BigDecimal("5.1")));
    }
}
