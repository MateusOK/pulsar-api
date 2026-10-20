package com.soupulsar.infrastructure.persistence.entity.partner;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@Table(name = "partners")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    private boolean active;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}