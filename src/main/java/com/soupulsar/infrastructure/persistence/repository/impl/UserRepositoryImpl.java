package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.UserRepository;
import com.soupulsar.infrastructure.persistence.entity.user.UserEntity;
import com.soupulsar.infrastructure.persistence.mapper.UserMapper;
import com.soupulsar.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {


    private final UserJpaRepository jpaRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        UserEntity saved = jpaRepository.save(userEntity);
        return UserMapper.toModel(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UserMapper::toModel);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return jpaRepository.findById(userId).map(UserMapper::toModel);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existsById(UUID userId) {
        return jpaRepository.existsById(userId);
    }
}
