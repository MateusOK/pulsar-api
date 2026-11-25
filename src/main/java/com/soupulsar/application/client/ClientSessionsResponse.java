package com.soupulsar.application.client;

import lombok.Builder;

import java.util.List;

@Builder
public record ClientSessionsResponse(
        List<ClientSessionResponse> futureSessions,
        List<ClientSessionResponse> history
) {
}
