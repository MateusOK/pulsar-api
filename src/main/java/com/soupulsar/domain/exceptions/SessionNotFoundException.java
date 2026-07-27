package com.soupulsar.domain.exceptions;

import java.util.UUID;

public class SessionNotFoundException extends NotFoundException {
    public SessionNotFoundException(UUID sessionId) { super("Session not found with ID: " + sessionId);}
    public SessionNotFoundException() { super("Session not found"); }
    public SessionNotFoundException(UUID specialistId, UUID sessionId) { super("Session not found for specialist " + specialistId + " and session " + sessionId); }
}
