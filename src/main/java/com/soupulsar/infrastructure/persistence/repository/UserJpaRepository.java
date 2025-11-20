package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByUserId(UUID userId);
    Optional<UserEntity> findByUserId(UUID userId);
    List<UserEntity> findAllByUserIdIn(List<UUID> userIds);

}