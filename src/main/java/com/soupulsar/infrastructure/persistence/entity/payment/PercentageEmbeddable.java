package com.soupulsar.infrastructure.persistence.entity.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class PercentageEmbeddable {

    @Column(name = "percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal value;
}