package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.CreateAvailabilityRequest;
import com.soupulsar.application.dto.response.CreateAvailabilityResponse;
import com.soupulsar.application.usecase.availability.CreateAvailabilityUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/availabilities")
@Tag(name = "Availabilities", description = "Endpoints for managing availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final CreateAvailabilityUseCase createAvailabilityUseCase;

    @Operation(summary = "Create Availability", description = "Create a new availability slot for a specialist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Availability created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<CreateAvailabilityResponse> createAvailability(@RequestBody CreateAvailabilityRequest request) {
        CreateAvailabilityResponse response = createAvailabilityUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/availabilities/" + response.id())).body(response);
    }


}
