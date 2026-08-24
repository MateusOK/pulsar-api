package com.soupulsar.api.controllers;

import com.soupulsar.application.specialist.availability.*;
import com.soupulsar.application.specialist.block.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/availabilities")
@Tag(name = "Availabilities", description = "Endpoints for managing availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final CreateAvailabilityUseCase createAvailabilityUseCase;
    private final UpdateAvailabilityUseCase updateAvailabilityUseCase;
    private final UpdateAvailabilityDayUseCase updateAvailabilityDayUseCase;
    private final DeleteAvailabilityUseCase deleteAvailabilityUseCase;
    private final CreateAvailabilityBlockUseCase createAvailabilityBlockUseCase;
    private final DeleteAvailabilityBlockUseCase deleteAvailabilityBlockUseCase;
    private final GetSpecialistAvailabilitiesUseCase getSpecialistAvailabilitiesUseCase;
    private final GetSpecialistAvailabilityBlocksUseCase getSpecialistAvailabilityBlocksUseCase;

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

    @Operation(summary = "Get Specialist Availabilities", description = "Retrieve all availability slots for a specialist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availabilities retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Specialist not found")
    })
    @GetMapping
    public ResponseEntity<GetSpecialistAvailabilitiesResponse> getSpecialistAvailabilities() {
        GetSpecialistAvailabilitiesResponse response = getSpecialistAvailabilitiesUseCase.execute();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update Availability", description = "Update an existing availability slot for a specialist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Availability not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAvailability(@PathVariable UUID id, @RequestBody UpdateAvailabilityRequest request) {
        updateAvailabilityUseCase.execute(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update Availability Day", description = "Update the day of an existing availability slot for a specialist, set false or true if the specialist is available on that day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability day updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Availability not found")
    })
    @PatchMapping
    public ResponseEntity<Void> updateAvailabilityDay(@RequestBody UpdateAvailabilityDayRequest request) {
        updateAvailabilityDayUseCase.execute(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete Availability", description = "Delete an existing availability slot for a specialist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Availability deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Availability not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete availability with conflicting future sessions")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable UUID id) {
        deleteAvailabilityUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get Specialist Availability Blocks", description = "Retrieve all availability blocks for a specialist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability blocks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Specialist not found"),
    })
    @GetMapping("/blocks")
    public ResponseEntity<GetSpecialistAvailabilityBlocksResponse> getSpecialistAvailabilityBlocks() {
        GetSpecialistAvailabilityBlocksResponse response = getSpecialistAvailabilityBlocksUseCase.execute();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create Availability Block", description = "Create a new availability block for a specialist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Availability block created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Availability block conflicts with existing availabilities or blocks")
    })
    @PostMapping("/blocks/")
    public ResponseEntity<CreateAvailabilityBlockResponse> createBlock(@RequestBody CreateAvailabilityBlockRequest request) {
        CreateAvailabilityBlockResponse response = createAvailabilityBlockUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/availabilities/blocks/" + response.id())).body(response);
    }

    @Operation(summary = "Delete Availability Block", description = "Delete an existing availability block for a specialist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Availability block deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Availability block not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete availability block with conflicting future sessions")
    })
    @DeleteMapping("/blocks/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable UUID id) {
        deleteAvailabilityBlockUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}