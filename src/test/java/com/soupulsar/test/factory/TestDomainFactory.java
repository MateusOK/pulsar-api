package com.soupulsar.test.factory;

import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.enums.PaymentMethod;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.model.vo.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public final class TestDomainFactory {
    private static final String DEFAULT_NAME = "Test User";
    private static final String DEFAULT_CPF = "00000000000";
    private static final String DEFAULT_TELEPHONE = "11999999999";
    private static final String DEFAULT_EMAIL = "test@test.com";
    private static final String DEFAULT_PASSWORD = "hash";
    private static final RegistrationNumber DEFAULT_REG = new RegistrationNumber("REG-123");
    private static final Address DEFAULT_ADDRESS = Address.builder()
            .street("street")
            .city("city")
            .state("state")
            .zipCode("00000-000")
            .neighbourhood("neigh")
            .build();

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2030, 1, 1, 10, 0);

    public static User simpleUser() {
        return User.create(DEFAULT_NAME, DEFAULT_CPF, DEFAULT_TELEPHONE, DEFAULT_EMAIL, DEFAULT_PASSWORD, UserRole.CLIENT,
                DEFAULT_ADDRESS);
    }

    public static ClientProfile client(UUID userId) {
        return ClientProfile.builder().profileId(UUID.randomUUID()).userId(userId).dateOfBirth(new Date()).build();
    }

    public static ClientProfile clientWithExternal(UUID userId, String externalId) {
        ClientProfile client = client(userId);
        client.attachExternalCustomerId(externalId);
        return client;
    }

    public static SpecialistProfile specialist(UUID userId, String walletId) {
        return SpecialistProfile.create(userId, DEFAULT_REG, Presentation.builder().about("p").build(), SpecialistType.PSICOLOGO, new Money(new BigDecimal("70.00")), walletId);
    }

    public static Payment simplePayment(UUID sessionId, UUID specialistId, UUID clientId) {
        Money original = new Money(new BigDecimal("100.00"));
        Money discount = new Money(new BigDecimal("0.00"));
        PaymentAmounts amounts = new PaymentAmounts(original, discount);
        PaymentSplit split = new PaymentSplit(new Money(new BigDecimal("30.00")), new Money(new BigDecimal("70.00")));
        return Payment.create(sessionId, specialistId, clientId, amounts, split, PaymentMethod.CREDIT_CARD);
    }

    public static Session futureSession(UUID specialistId, UUID clientId) {
        return Session.scheduleSession(specialistId, clientId, FIXED_TIME.plusDays(2), FIXED_TIME.plusDays(2).plusHours(1));
    }
}