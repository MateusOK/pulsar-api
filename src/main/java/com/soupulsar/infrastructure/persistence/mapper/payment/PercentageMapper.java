package com.soupulsar.infrastructure.persistence.mapper.payment;

import com.soupulsar.domain.model.vo.Percentage;
import com.soupulsar.infrastructure.persistence.entity.payment.PercentageEmbeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PercentageMapper {

    public static PercentageEmbeddable toEmbeddable(Percentage percentage) {
        if (percentage == null) return null;
        return new PercentageEmbeddable(percentage.value());
    }

    public static Percentage toValueObject(PercentageEmbeddable percentageEmbeddable) {
        if (percentageEmbeddable == null) return null;
        return new Percentage(percentageEmbeddable.getValue());
    }
}
