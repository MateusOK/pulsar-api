package com.soupulsar.application.client;

import lombok.Builder;

@Builder
public record HomeResponse(ClientNextAppointmentResponse nextAppointment) {
}
