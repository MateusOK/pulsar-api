package com.soupulsar.modulith.scheduling.domain.model;

import com.soupulsar.modulith.scheduling.domain.model.enums.SessionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

 /**
  * Represents a session between a specialist and a client.
  */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Session {

    private final UUID sessionId;
    private final UUID specialistId;
    private final UUID clientId;

    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    private SessionStatus status;


    public static Session scheduleSession(UUID specialistId, UUID clientId, LocalDateTime startAt, LocalDateTime endAt) {
        return new Session(UUID.randomUUID(), specialistId, clientId, startAt, endAt, SessionStatus.SCHEDULING);
    }

    public static Session restore(UUID sessionId, UUID specialistId, UUID clientId, LocalDateTime startAt, LocalDateTime endAt, SessionStatus status) {
        return new Session(sessionId, specialistId, clientId, startAt, endAt, status);
    }

    public void confirmPayment() {
        if (this.status != SessionStatus.AWAITING_PAYMENT) {
            throw new IllegalStateException("Payment can only be confirmed for sessions awaiting payment.");
        }
        this.status = SessionStatus.CONFIRMED;
    }

    public void cancelSession() {
        if (this.status == SessionStatus.CANCELLED || this.status == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a session that is already cancelled or completed.");
        }
        this.status = SessionStatus.CANCELLED;
    }

    public void completeSession() {
        if (this.status != SessionStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed sessions can be completed.");
        }
        this.status = SessionStatus.COMPLETED;
    }

    public boolean overlaps(LocalDateTime startAt, LocalDateTime endAt) {
        return this.startAt.isBefore(endAt) && startAt.isBefore(this.endAt);
    }

}