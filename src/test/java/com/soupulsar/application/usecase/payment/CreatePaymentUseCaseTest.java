package com.soupulsar.application.usecase.payment;

import com.soupulsar.application.dto.request.CreatePaymentRequest;
import com.soupulsar.domain.exceptions.PaymentSplitRuleNotFoundException;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.payment.PaymentSplitRule;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.Percentage;
import com.soupulsar.domain.repository.*;
import com.soupulsar.domain.model.enums.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePaymentUseCaseTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentSplitRuleRepository paymentSplitRuleRepository;

    @Mock
    private SpecialistProfileRepository specialistProfileRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreatePaymentUseCase useCase;

    @Test
    void ShouldSavePaymentWhenRequestIsValid() {
        UUID sessionId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        Money price = new Money(new BigDecimal("100.00"));
        Money discount = new Money(new BigDecimal("10.00"));
        CreatePaymentRequest request = new CreatePaymentRequest(
                sessionId,
                specialistId,
                clientId,
                price,
                discount,
                PaymentMethod.CREDIT_CARD
        );

        Session session = mock(Session.class);
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));
        when(session.isAwaitingPayment()).thenReturn(true);
        when(session.belongsTo(clientId, specialistId)).thenReturn(true);

        User specialistUser = mock(User.class);
        User clientUser = mock(User.class);
        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialistUser));
        when(userRepository.findById(clientId)).thenReturn(Optional.of(clientUser));

        SpecialistProfile profile = mock(SpecialistProfile.class);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.of(profile));

        PaymentSplitRule rule = mock(PaymentSplitRule.class);
        Percentage percentage = mock(Percentage.class);
        when(rule.getPlatformPercentage()).thenReturn(percentage);

        Money finalAmount = new Money(new BigDecimal("90.00"));
        Money platformAmount = new Money(new BigDecimal("9.00"));
        when(percentage.applyTo((finalAmount))).thenReturn(platformAmount);

        when(paymentSplitRuleRepository.findActiveApplicableRules(eq(specialistId), any()))
                .thenReturn(List.of(rule));

        useCase.execute(request);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(captor.capture());

        Payment saved = captor.getValue();
        assertNotNull(saved);
    }

    @Test
    void ShouldThrowUserNotFoundWhenSpecialistNotFound() {
        UUID sessionId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        Money price = new Money(new BigDecimal("50.00"));
        Money discount = Money.zero();
        CreatePaymentRequest request = new CreatePaymentRequest(
                sessionId,
                specialistId,
                clientId,
                price,
                discount,
                PaymentMethod.CREDIT_CARD
        );

        Session session = mock(Session.class);
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));

        when(userRepository.findById(specialistId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(request));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void ShouldThrowPaymentSplitRuleNotFoundWhenNoRules() {
        UUID sessionId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        Money price = new Money(new BigDecimal("70.00"));
        Money discount = new Money(new BigDecimal("20.00"));
        CreatePaymentRequest request = new CreatePaymentRequest(
                sessionId,
                specialistId,
                clientId,
                price,
                discount,
                PaymentMethod.CREDIT_CARD
        );

        Session session = mock(Session.class);
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));
        when(session.isAwaitingPayment()).thenReturn(true);
        when(session.belongsTo(clientId, specialistId)).thenReturn(true);

        SpecialistProfile profile = mock(SpecialistProfile.class);
        when(specialistProfileRepository.findById(specialistId)).thenReturn(Optional.of(profile));

        User specialistUser = mock(User.class);
        User clientUser = mock(User.class);
        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialistUser));
        when(userRepository.findById(clientId)).thenReturn(Optional.of(clientUser));

        when(paymentSplitRuleRepository.findActiveApplicableRules(eq(specialistId), any()))
                .thenReturn(List.of());

        assertThrows(PaymentSplitRuleNotFoundException.class, () -> useCase.execute(request));
        verify(paymentRepository, never()).save(any());
    }
}
