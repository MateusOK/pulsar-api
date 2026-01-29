package com.soupulsar.application.usecase.payment;

import com.soupulsar.application.dto.request.CreatePaymentRequest;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.payment.PaymentSplitRule;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.PaymentAmounts;
import com.soupulsar.domain.model.vo.PaymentSplit;
import com.soupulsar.domain.repository.PaymentRepository;
import com.soupulsar.domain.repository.PaymentSplitRuleRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
public class CreatePaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentSplitRuleRepository paymentSplitRuleRepository;
    private final SpecialistProfileRepository specialistProfileRepository;

    public void execute(CreatePaymentRequest request) {

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
    }

    private PaymentSplitRule getPaymentSplitRule(UUID specialistId) {

        SpecialistProfile profile = specialistProfileRepository.findById(specialistId)
                .orElseThrow(() -> new UserNotFoundException(specialistId));

        List<PaymentSplitRule> rules = paymentSplitRuleRepository
                .findActiveApplicableRules(specialistId, profile.getSpecialistType());

        if (rules.isEmpty()) {
            throw new IllegalStateException("No applicable payment split rules found for specialist: " + specialistId);
        }

        return rules.stream()
                .min(Comparator.comparingInt(r -> r.getScope().getPriority()))
                .orElseThrow(() -> new IllegalStateException("No applicable payment split rules found for specialist: " + specialistId));
    }
}