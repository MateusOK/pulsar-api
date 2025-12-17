package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.GetAllSpecialistRequest;
import com.soupulsar.application.dto.response.DailyAvailabilityResponse;
import com.soupulsar.application.dto.response.GetAllSpecialistsResponse;
import com.soupulsar.application.dto.response.SpecialistDetailsResponse;
import com.soupulsar.application.usecase.specialist.GetAllSpecialistsUseCase;
import com.soupulsar.application.usecase.specialist.GetDailyAvailabilityUseCase;
import com.soupulsar.application.usecase.specialist.GetSpecialistDetailsUseCase;
import com.soupulsar.domain.model.enums.SpecialistType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/specialists")
@Tag(name = "Specialists", description = "Endpoints for managing specialists")
@RequiredArgsConstructor
public class SpecialistsController {

    private final GetAllSpecialistsUseCase getAllSpecialistsUseCase;
    private final GetSpecialistDetailsUseCase getSpecialistDetailsUseCase;
    private final GetDailyAvailabilityUseCase getDailyAvailabilityUseCase;

    @Operation(summary = "Get All Specialists", description = "Retrieve a paginated list of specialists with optional filtering by type and price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specialists retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping
    public ResponseEntity<Page<GetAllSpecialistsResponse>> getAllSpecialists(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) SpecialistType specialistType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        return ResponseEntity.ok(getAllSpecialistsUseCase.execute(GetAllSpecialistRequest.of(specialistType, minPrice, maxPrice, page, size)));
    }

    @Operation(summary = "Get Specialist Details", description = "Retrieve detailed information about a specific specialist by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specialist details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Specialist not found")
    })
    @GetMapping("/{specialistId}")
    public ResponseEntity<SpecialistDetailsResponse> getSpecialistDetails(@PathVariable UUID specialistId) {
        var response = getSpecialistDetailsUseCase.execute(specialistId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Specialist Daily Availability", description = "Retrieve the daily availability of a specific specialist on a given date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specialist daily availability retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Specialist not found")
    })
    @GetMapping("/{specialistId}/availability/{date}")
    public ResponseEntity<DailyAvailabilityResponse> getSpecialistDailyAvailability(@PathVariable UUID specialistId, @PathVariable LocalDate date) {
        var response =getDailyAvailabilityUseCase.execute(specialistId, date);
        return ResponseEntity.ok(response);
    }
}