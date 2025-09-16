package com.soupulsar.modulith.scheduling.api.controllers;

import com.soupulsar.modulith.scheduling.application.dto.ScheduleSessionRequest;
import com.soupulsar.modulith.scheduling.application.dto.ScheduleSessionResponse;
import com.soupulsar.modulith.scheduling.application.usecase.ScheduleSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final ScheduleSessionUseCase scheduleSessionUseCase;

    @PostMapping
    public ResponseEntity<ScheduleSessionResponse> scheduleSession(@RequestBody ScheduleSessionRequest request) {
        ScheduleSessionResponse response = scheduleSessionUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/sessions/" + response.sessionId())).body(response);
    }

}