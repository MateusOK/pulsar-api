package com.soupulsar.modulith.scheduling.api.controllers;

import com.soupulsar.modulith.scheduling.application.dto.ScheduleSessionRequest;
import com.soupulsar.modulith.scheduling.application.dto.ScheduleSessionResponse;
import com.soupulsar.modulith.scheduling.application.dto.SessionResponse;
import com.soupulsar.modulith.scheduling.application.usecase.CancelSessionUseCase;
import com.soupulsar.modulith.scheduling.application.usecase.ScheduleSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final ScheduleSessionUseCase scheduleSessionUseCase;
    private final CancelSessionUseCase cancelSessionUseCase;

    @PostMapping
    public ResponseEntity<ScheduleSessionResponse> scheduleSession(@RequestBody ScheduleSessionRequest request) {
        ScheduleSessionResponse response = scheduleSessionUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/sessions/" + response.sessionId())).body(response);
    }

    @PatchMapping("/{sessionId}/cancel")
    public ResponseEntity<SessionResponse> cancelSession(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(cancelSessionUseCase.execute(sessionId));
    }

}