package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.client.ClientProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientProfileJpaRepository extends JpaRepository<ClientProfileEntity, UUID> {
}
