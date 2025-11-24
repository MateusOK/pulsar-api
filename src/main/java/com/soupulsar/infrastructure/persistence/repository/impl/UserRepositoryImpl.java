package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.UserRepository;
import com.soupulsar.infrastructure.persistence.entity.user.UserEntity;
import com.soupulsar.infrastructure.persistence.mapper.UserMapper;
import com.soupulsar.infrastructure.persistence.repository.UserJpaRepository;
import com.soupulsar.infrastructure.persistence.specification.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return jpaRepository.findByUserId(userId).map(UserMapper::toModel);
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
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public Page<User> findAll(UserStatus status, UserRole role, Pageable pageable) {
        Specification<UserEntity> spec = Specification.unrestricted();

        if (status != null) {
            spec = spec.and(UserSpecifications.hasStatus(status));
        }

        if (role != null) {
            spec = spec.and(UserSpecifications.hasRole(role));
        }
        return jpaRepository.findAll(spec, pageable).map(UserMapper::toModel);
    }

    @Override
    public Map<UUID, User> findAllByUserId(List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) return  Collections.emptyMap();
        return jpaRepository.findAllByUserIdIn(userIds)
                .stream()
                .collect(Collectors.toMap(UserEntity::getUserId, UserMapper::toModel));
    }
}
