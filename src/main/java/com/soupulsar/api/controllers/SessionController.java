package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.ScheduleSessionRequest;
import com.soupulsar.application.dto.response.ScheduleSessionResponse;
import com.soupulsar.application.dto.response.SessionResponse;
import com.soupulsar.application.usecase.session.CancelSessionUseCase;
import com.soupulsar.application.usecase.session.ScheduleSessionUseCase;
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