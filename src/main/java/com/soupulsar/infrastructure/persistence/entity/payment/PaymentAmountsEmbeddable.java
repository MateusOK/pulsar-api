package com.soupulsar.infrastructure.persistence.entity.payment;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PaymentAmountsEmbeddable {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "original_amount", precision = 10, scale = 2))
    MoneyEmbeddable originalAmount;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "discount_amount", precision = 10, scale = 2))
    MoneyEmbeddable discountAmount;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "final_amount", precision = 10, scale = 2))
    MoneyEmbeddable finalAmount;
}