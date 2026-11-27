package com.soupulsar.infrastructure.persistence.entity.review;

import com.soupulsar.domain.model.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "reviews")
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ReviewEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID specialistId;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false, length = 280)
    private String comment;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReviewStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

}