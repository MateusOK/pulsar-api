package com.soupulsar.application.specialist.shared.summary;

import com.soupulsar.application.specialist.shared.daterange.DateRange;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionStatisticsCalculator {

    private final SessionRepository sessionRepository;

    public SessionSummary calculate(UUID specialistId, DateRange dateRange) {

        long completed = sessionRepository.countSessionsByStatus(specialistId, List.of(SessionStatus.COMPLETED), dateRange.start(), dateRange.end());

        long confirmed = sessionRepository.countSessionsByStatus(specialistId, List.of(SessionStatus.CONFIRMED), dateRange.start(), dateRange.end());

        return new SessionSummary(completed, confirmed);
    }
}