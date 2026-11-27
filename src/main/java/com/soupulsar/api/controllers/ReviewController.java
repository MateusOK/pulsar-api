package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.CreateReviewRequest;
import com.soupulsar.application.dto.response.CreateReviewResponse;
import com.soupulsar.application.review.CreateReviewUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Endpoints for managing specialist reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final CreateReviewUseCase createReviewUseCase;

    @Operation(summary = "Create Review", description = "Submit a review for a specialist by the currently authenticated client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review submitted successfully", content = @Content(schema = @Schema(implementation = CreateReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid review data or sensitive information detected"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Specialist not found"),
            @ApiResponse(responseCode = "409", description = "Client has no completed session with this specialist"),
            @ApiResponse(responseCode = "503", description = "Review moderation service unavailable")
    })
    @PostMapping
    public ResponseEntity<CreateReviewResponse> createReview(@RequestBody @Valid CreateReviewRequest request) {
        CreateReviewResponse response = createReviewUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/reviews/" + response.reviewId())).body(response);
    }
}
