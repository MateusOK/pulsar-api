package com.soupulsar.application.specialist.block;

import com.soupulsar.domain.model.availability.AvailabilityBlock;
import java.util.List;
public record GetSpecialistAvailabilityBlocksResponse(
        List<AvailabilityBlock> blocks
) {
}