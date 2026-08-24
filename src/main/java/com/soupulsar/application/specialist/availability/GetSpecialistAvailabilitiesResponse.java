package com.soupulsar.application.specialist.availability;

import com.soupulsar.domain.model.availability.Availability;
import java.util.List;

public record GetSpecialistAvailabilitiesResponse(
        List<Availability> availabilities
) {
}