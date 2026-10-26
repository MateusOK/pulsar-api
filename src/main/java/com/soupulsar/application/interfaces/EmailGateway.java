package com.soupulsar.application.interfaces;

import java.time.Instant;

public interface EmailGateway {

    void sendPasswordResetEmail(String resetLink, String name, String email);

    void sendPartnerDiscountCodeEmail(String code, String email, Instant expiresAt);

}