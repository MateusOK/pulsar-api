package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID userId);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsById(UUID userId);
    Page<User> findAll(UserStatus status, UserRole role, Pageable pageable);
    Map<UUID, User> findAllByUserId(List<UUID> specialistIds);
}
