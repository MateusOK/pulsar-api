package com.soupulsar.domain.payment;

import com.soupulsar.domain.model.enums.PaymentSplitScope;
import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.payment.PaymentSplitRule;
import com.soupulsar.domain.model.vo.Percentage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentSplitRuleTest {

    @Test
    void createSpecialistRuleSuccess() {
        UUID specialistId = UUID.randomUUID();
        Percentage pct = new Percentage(new BigDecimal("10.00"));

        PaymentSplitRule rule = PaymentSplitRule.createSpecialistRule(specialistId, pct);

        assertNotNull(rule.getId());
        assertEquals(PaymentSplitScope.SPECIALIST, rule.getScope());
        assertEquals(specialistId, rule.getSpecialistId());
        assertTrue(rule.isActive());
    }

    @Test
    void createSpecialistRuleNullIdThrows() {
        Percentage pct = new Percentage(new BigDecimal("5.00"));

        assertThrows(IllegalArgumentException.class, () -> PaymentSplitRule.createSpecialistRule(null, pct));
    }

    @Test
    void createSpecialistTypeAndGlobalRules() {
        Percentage pct = new Percentage(new BigDecimal("15.00"));
        PaymentSplitRule typeRule = PaymentSplitRule.createSpecialistTypeRule(SpecialistType.PSICOLOGO, pct);
        assertEquals(PaymentSplitScope.SPECIALIST_TYPE, typeRule.getScope());

        PaymentSplitRule globalRule = PaymentSplitRule.createGlobalRule(pct);
        assertEquals(PaymentSplitScope.GLOBAL, globalRule.getScope());
    }

    @Test
    void deactivateWorks() {
        Percentage pct = new Percentage(new BigDecimal("12.00"));
        PaymentSplitRule rule = PaymentSplitRule.createGlobalRule(pct);
        assertTrue(rule.isActive());
        rule.deactivate();
        assertFalse(rule.isActive());
    }
}

