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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class PaymentSplitEmbeddable {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "platform_amount", precision = 10, scale = 2))
    private MoneyEmbeddable platformAmount;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "specialist_amount", precision = 10, scale = 2))
    private MoneyEmbeddable specialistAmount;

    private MoneyEmbeddable finalAmount;


}
