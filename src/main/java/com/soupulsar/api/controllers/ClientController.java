package com.soupulsar.api.controllers;

import com.soupulsar.application.client.ClientSessionsResponse;
import com.soupulsar.application.client.GetClientNextAppointmentUseCase;
import com.soupulsar.application.client.GetClientSessionsUseCase;
import com.soupulsar.application.client.HomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Endpoints for client operations")
@RequiredArgsConstructor
public class ClientController {

    private final GetClientSessionsUseCase getClientSessionsUseCase;
    private final GetClientNextAppointmentUseCase getClientNextAppointmentUseCase;

    @Operation(summary = "Get Client Home", description = "Retrieve the next upcoming appointment for the currently authenticated client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Home data retrieved successfully", content = @Content(schema = @Schema(implementation = HomeResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me/home")
    public ResponseEntity<HomeResponse> getClientHome() {
        return ResponseEntity.ok(getClientNextAppointmentUseCase.execute());
    }

    @Operation(summary = "Get Client Sessions", description = "Retrieve all future sessions and session history for the currently authenticated client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessions retrieved successfully", content = @Content(schema = @Schema(implementation = ClientSessionsResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me/sessions")
    public ResponseEntity<ClientSessionsResponse> getClientSessions() {
        return ResponseEntity.ok(getClientSessionsUseCase.execute());
    }
}
