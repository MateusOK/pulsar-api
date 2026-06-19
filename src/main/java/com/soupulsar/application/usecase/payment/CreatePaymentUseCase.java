package com.soupulsar.application.usecase.payment;

import com.soupulsar.application.dto.request.CreatePaymentRequest;
import com.soupulsar.domain.exceptions.InvalidSessionStateForPaymentException;
import com.soupulsar.domain.exceptions.PaymentSplitRuleNotFoundException;
import com.soupulsar.domain.exceptions.SessionNotFoundException;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.payment.PaymentSplitRule;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.PaymentAmounts;
import com.soupulsar.domain.model.vo.PaymentSplit;
import com.soupulsar.domain.repository.PaymentRepository;
import com.soupulsar.domain.repository.PaymentSplitRuleRepository;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CreatePaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentSplitRuleRepository paymentSplitRuleRepository;
    private final SpecialistProfileRepository specialistProfileRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public UUID execute(CreatePaymentRequest request) {

        validateCreationParams(request.sessionId(), request.specialistId(), request.clientId());

        PaymentAmounts amounts = new PaymentAmounts(request.price(), request.discount());
        PaymentSplitRule splitRule = getPaymentSplitRule(request.specialistId());
        Money platformAmount = splitRule.getPlatformPercentage().applyTo(amounts.getFinalAmount());
        Money specialistAmount = amounts.getFinalAmount().subtract(platformAmount);
        PaymentSplit split = new PaymentSplit(platformAmount, specialistAmount);

        Payment payment = Payment.create(
                request.sessionId(),
                request.specialistId(),
                request.clientId(),
                amounts,
                split,
                request.paymentMethod()
        );
        paymentRepository.save(payment);

        log.info(
                "Payment created: paymentId={}, sessionId={}, specialistId={}, clientId={}, finalAmount={}",
                payment.getId(),
                request.sessionId(),
                request.specialistId(),
                request.clientId(),
                amounts.getFinalAmount()
        );
        return payment.getId();
    }

    private PaymentSplitRule getPaymentSplitRule(UUID specialistId) {

        SpecialistProfile profile = specialistProfileRepository.findById(specialistId)
                .orElseThrow(() -> new UserNotFoundException(specialistId));

        List<PaymentSplitRule> rules = paymentSplitRuleRepository
                .findActiveApplicableRules(specialistId, profile.getSpecialistType());

        if (rules.isEmpty()) {
            throw new PaymentSplitRuleNotFoundException();
        }

        return rules.stream()
                .min(Comparator.comparingInt(r -> r.getScope().getPriority()))
                .orElseThrow(PaymentSplitRuleNotFoundException::new);
    }

    private void validateCreationParams(UUID sessionId, UUID specialistId, UUID clientId) {

        Session session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        userRepository.findById(specialistId)
                .orElseThrow(() -> new UserNotFoundException(specialistId));

        userRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));

        if (!session.isAwaitingPayment()){
            throw new InvalidSessionStateForPaymentException("Session status invalid for payment");
        }

        if (!session.belongsTo(clientId, specialistId)){
            throw new IllegalStateException("Session does not belong to users");
        }
    }
}