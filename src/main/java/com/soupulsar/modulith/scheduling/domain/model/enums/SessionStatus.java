package com.soupulsar.modulith.scheduling.domain.model.enums;

/**
 * Represents the possible states of a session.
 */
public enum SessionStatus {

    /**
     * Session created but not yet scheduled.
     */
    SCHEDULING,

    /**
     * Waiting payment from the client to confirm the session.
     */
    AWAITING_PAYMENT,

    /**
     * Payment received and session confirmed.
     */
    CONFIRMED,

    /**
     * Session cancelled by either the client or the specialist.
     */
    CANCELLED,

    /**
     * Session completed successfully.
     */
    COMPLETED
}