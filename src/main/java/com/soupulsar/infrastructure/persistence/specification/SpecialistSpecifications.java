package com.soupulsar.infrastructure.persistence.specification;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.infrastructure.persistence.entity.specialist.SpecialistProfileEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecialistSpecifications {

    public static Specification<SpecialistProfileEntity> hasType(SpecialistType specialistType) {
        return (root, criteriaQuery, criteriaBuilder) ->
                specialistType == null ? null : criteriaBuilder.equal(root.get("specialistType"), specialistType);
    }

    public static Specification<SpecialistProfileEntity> hasPriceRange(BigDecimal min, BigDecimal max) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (min != null && max != null) return criteriaBuilder.between(root.get("sessionPrice"), min, max);
            if (min != null) return criteriaBuilder.greaterThanOrEqualTo(root.get("sessionPrice"), min);
            if (max != null) return criteriaBuilder.lessThanOrEqualTo(root.get("sessionPrice"), max);
            return criteriaBuilder.conjunction();
        };
    }
}