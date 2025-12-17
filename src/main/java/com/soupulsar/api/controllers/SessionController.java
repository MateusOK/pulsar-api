package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.ScheduleSessionRequest;
import com.soupulsar.application.dto.response.ScheduleSessionResponse;
import com.soupulsar.application.dto.response.SessionResponse;
import com.soupulsar.application.usecase.session.CancelSessionUseCase;
import com.soupulsar.application.usecase.session.ScheduleSessionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Sessions", description = "Endpoints for managing sessions")
@RequiredArgsConstructor
public class SessionController {

    private final ScheduleSessionUseCase scheduleSessionUseCase;
    private final CancelSessionUseCase cancelSessionUseCase;

    @Operation(summary = "Schedule a Session", description = "Schedule a new session with a specialist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Session scheduled successfully", content = @Content(schema = @Schema(implementation = ScheduleSessionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<ScheduleSessionResponse> scheduleSession(@RequestBody ScheduleSessionRequest request) {
        ScheduleSessionResponse response = scheduleSessionUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/sessions/" + response.sessionId())).body(response);
    }

    @Operation(summary = "Cancel a Session", description = "Cancel an existing session by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session canceled successfully", content = @Content(schema = @Schema(implementation = SessionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @PatchMapping("/{sessionId}/cancel")
    public ResponseEntity<SessionResponse> cancelSession(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(cancelSessionUseCase.execute(sessionId));
    }

}