package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.auth.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetTokenEntity, UUID> {

    Optional<PasswordResetTokenEntity> findByToken(String token);
}