package com.soupulsar.modulith.scheduling.api.controllers;

import com.soupulsar.modulith.scheduling.application.dto.CreateAvailabilityRequest;
import com.soupulsar.modulith.scheduling.application.dto.CreateAvailabilityResponse;
import com.soupulsar.modulith.scheduling.application.usecase.CreateAvailabilityUseCase;
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
