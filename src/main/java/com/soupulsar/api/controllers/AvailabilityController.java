package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.CreateAvailabilityRequest;
import com.soupulsar.application.dto.response.CreateAvailabilityResponse;
import com.soupulsar.application.usecase.availability.CreateAvailabilityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final CreateAvailabilityUseCase createAvailabilityUseCase;

    @PostMapping
    public ResponseEntity<CreateAvailabilityResponse> createAvailability(@RequestBody CreateAvailabilityRequest request) {
        CreateAvailabilityResponse response = createAvailabilityUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/availabilities/" + response.id())).body(response);
    }


}
