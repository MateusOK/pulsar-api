package com.soupulsar.domain.model.availability;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AvailabilityBlock {

    private final UUID id;
    private final UUID specialistId;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final String reason;

    public static AvailabilityBlock create(UUID specialistId, LocalDateTime startsAt, LocalDateTime endsAt, String reason) {
        if (!endsAt.isAfter(startsAt)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        return new AvailabilityBlockBuilder()
                .id(UUID.randomUUID())
                .specialistId(specialistId)
                .startsAt(startsAt)
                .endsAt(endsAt)
                .reason(reason)
                .build();
    }

    public static AvailabilityBlock restore(UUID id, UUID specialistId, LocalDateTime startsAt, LocalDateTime endsAt, String reason) {
        return new AvailabilityBlock(id, specialistId, startsAt, endsAt, reason);
    }

    public boolean overlaps(AvailabilityBlock other) {
        return startsAt.isBefore(other.endsAt) && endsAt.isAfter(other.startsAt);
    }
}