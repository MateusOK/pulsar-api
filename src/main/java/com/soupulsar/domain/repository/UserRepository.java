package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.user.User;

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
