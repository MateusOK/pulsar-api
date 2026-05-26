package com.soupulsar.domain.session;

import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.session.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    void scheduleSessionCreatesAwaitingPayment() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);

        Session s = Session.scheduleSession(specialistId, clientId, start, end);

        assertNotNull(s.getSessionId());
        assertEquals(specialistId, s.getSpecialistId());
        assertEquals(clientId, s.getClientId());
        assertEquals(start, s.getStartAt());
        assertEquals(end, s.getEndAt());
        assertEquals(SessionStatus.AWAITING_PAYMENT, s.getStatus());
        assertTrue(s.isAwaitingPayment());
    }

    @Test
    void restoreReturnsGivenState() {
        UUID sid = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Session s = Session.restore(sid, specialistId, clientId, now, now.plusHours(1), SessionStatus.CONFIRMED);

        assertEquals(sid, s.getSessionId());
        assertEquals(SessionStatus.CONFIRMED, s.getStatus());
    }

    @Test
    void confirmPaymentTransitionsWhenAwaiting() {
        Session s = Session.scheduleSession(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        s.confirmPayment();
        assertEquals(SessionStatus.CONFIRMED, s.getStatus());
    }

    @Test
    void confirmPaymentThrowsIfNotAwaiting() {
        Session s = Session.restore(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), SessionStatus.CONFIRMED);
        assertThrows(IllegalStateException.class, s::confirmPayment);
    }

    @Test
    void cancelSessionAllowedUnlessAlreadyCancelledOrCompleted() {
        // cancel from awaiting
        Session s1 = Session.scheduleSession(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        s1.cancelSession();
        assertEquals(SessionStatus.CANCELLED, s1.getStatus());

        // cancel from confirmed
        Session s2 = Session.restore(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), SessionStatus.CONFIRMED);
        s2.cancelSession();
        assertEquals(SessionStatus.CANCELLED, s2.getStatus());
    }

    @Test
    void cancelSessionThrowsWhenAlreadyCancelledOrCompleted() {
        Session cancelled = Session.restore(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), SessionStatus.CANCELLED);
        assertThrows(IllegalStateException.class, cancelled::cancelSession);

        Session completed = Session.restore(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), SessionStatus.COMPLETED);
        assertThrows(IllegalStateException.class, completed::cancelSession);
    }

    @Test
    void completeSessionRequiresConfirmed() {
        Session s = Session.restore(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), SessionStatus.CONFIRMED);
        s.completeSession();
        assertEquals(SessionStatus.COMPLETED, s.getStatus());
    }

    @Test
    void completeSessionThrowsIfNotConfirmed() {
        Session s = Session.scheduleSession(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        assertThrows(IllegalStateException.class, s::completeSession);
    }

    @Test
    void overlapsDetectsOverlapAndNonOverlap() {
        LocalDateTime aStart = LocalDateTime.of(2026, 5, 12, 9, 0);
        LocalDateTime aEnd = aStart.plusHours(2); // 9-11
        Session a = Session.restore(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), aStart, aEnd, SessionStatus.AWAITING_PAYMENT);

        // overlapping: 10-12
        assertTrue(a.overlaps(LocalDateTime.of(2026,5,12,10,0), LocalDateTime.of(2026,5,12,12,0)));

        // non overlapping: 11-12 (end at 11 equals start at 11 -> not overlap)
        assertFalse(a.overlaps(LocalDateTime.of(2026,5,12,11,0), LocalDateTime.of(2026,5,12,12,0)));
    }

    @Test
    void belongsToChecksClientAndSpecialist() {
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        Session s = Session.scheduleSession(specialistId, clientId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));

        assertTrue(s.belongsTo(clientId, specialistId));
        assertFalse(s.belongsTo(UUID.randomUUID(), specialistId));
    }
}

