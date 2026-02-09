package com.soupulsar.domain.exceptions;

import java.util.UUID;

public class SessionNotFoundException extends RuntimeException{
    public SessionNotFoundException(UUID sessionId) { super("Session not found with ID: " + sessionId);}
    public SessionNotFoundException() { super("Session not found"); }
}
