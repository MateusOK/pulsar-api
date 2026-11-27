package com.soupulsar.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Request DTO for creating a specialist review")
public record CreateReviewRequest(

        @Schema(description = "ID of the specialist being reviewed", example = "660e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Specialist ID cannot be null")
        UUID specialistId,

        @Schema(description = "Rating from 0 to 5", example = "4.5", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Rating cannot be null")
        @DecimalMin(value = "0.0", message = "Rating must be at least 0")
        @DecimalMax(value = "5.0", message = "Rating must be at most 5")
        BigDecimal rating,

        @Schema(description = "Review comment (max 280 characters)", example = "Excelente profissional, muito atencioso.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Comment cannot be blank")
        @Size(max = 280, message = "Comment cannot exceed 280 characters")
        String comment
) {
}
