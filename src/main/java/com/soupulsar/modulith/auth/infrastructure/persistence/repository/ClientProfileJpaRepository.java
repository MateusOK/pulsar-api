package com.soupulsar.modulith.auth.infrastructure.persistence.repository;

import com.soupulsar.modulith.auth.infrastructure.persistence.entity.ClientProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientProfileJpaRepository extends JpaRepository<ClientProfileEntity, UUID> {
}
