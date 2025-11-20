package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.GetAllSpecialistRequest;
import com.soupulsar.application.dto.response.DailyAvailabilityResponse;
import com.soupulsar.application.dto.response.GetAllSpecialistsResponse;
import com.soupulsar.application.dto.response.SpecialistDetailsResponse;
import com.soupulsar.application.usecase.specialist.GetAllSpecialistsUseCase;
import com.soupulsar.application.usecase.specialist.GetDailyAvailabilityUseCase;
import com.soupulsar.application.usecase.specialist.GetSpecialistDetailsUseCase;
import com.soupulsar.domain.model.enums.SpecialistType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/specialists")
@RequiredArgsConstructor
public class SpecialistsController {

    private final GetAllSpecialistsUseCase getAllSpecialistsUseCase;
    private final GetSpecialistDetailsUseCase getSpecialistDetailsUseCase;
    private final GetDailyAvailabilityUseCase getDailyAvailabilityUseCase;

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

    @GetMapping("/{specialistId}")
    public ResponseEntity<SpecialistDetailsResponse> getSpecialistDetails(@PathVariable UUID specialistId) {
        var response = getSpecialistDetailsUseCase.execute(specialistId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{specialistId}/availability/{date}")
    public ResponseEntity<DailyAvailabilityResponse> getSpecialistDailyAvailability(@PathVariable UUID specialistId, @PathVariable LocalDate date) {
        var response =getDailyAvailabilityUseCase.execute(specialistId, date);
        return ResponseEntity.ok(response);
    }
}