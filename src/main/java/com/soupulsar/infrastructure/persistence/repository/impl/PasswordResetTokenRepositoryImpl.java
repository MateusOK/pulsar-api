package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.auth.PasswordResetToken;
import com.soupulsar.domain.repository.PasswordResetTokenRepository;
import com.soupulsar.infrastructure.persistence.entity.auth.PasswordResetTokenEntity;
import com.soupulsar.infrastructure.persistence.mapper.auth.PasswordResetTokenMapper;
import com.soupulsar.infrastructure.persistence.repository.PasswordResetTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private final PasswordResetTokenJpaRepository jpaRepository;

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        PasswordResetTokenEntity entity = PasswordResetTokenMapper.toEntity(passwordResetToken);
        PasswordResetTokenEntity saved = jpaRepository.save(entity);
        return PasswordResetTokenMapper.toModel(saved);
    }

    @Override
    public Optional<PasswordResetToken> findById(UUID uuid) {
        return jpaRepository.findById(uuid)
                .map(PasswordResetTokenMapper::toModel);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpaRepository.findByToken(token)
                .map(PasswordResetTokenMapper::toModel);
    }
}