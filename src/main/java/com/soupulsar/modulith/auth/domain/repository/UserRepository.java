package com.soupulsar.modulith.auth.domain.repository;

import com.soupulsar.modulith.auth.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID userId);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsById(UUID userId);
}
