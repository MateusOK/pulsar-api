package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.Presentation;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record GetAllSpecialistsResponse(
        UUID id,
        String name,
        String city,
        String state,
        Presentation presentation,
        Money price,
        Long sessionsCompleted
) {
}