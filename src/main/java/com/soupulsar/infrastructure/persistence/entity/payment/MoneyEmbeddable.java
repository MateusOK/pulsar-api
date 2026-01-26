package com.soupulsar.infrastructure.persistence.entity.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class MoneyEmbeddable {

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal value;
}
