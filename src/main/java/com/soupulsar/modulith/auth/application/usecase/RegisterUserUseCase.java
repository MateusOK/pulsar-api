package com.soupulsar.modulith.auth.application.usecase;

import com.soupulsar.modulith.auth.application.dto.CreateUserRequest;
import com.soupulsar.modulith.auth.application.dto.CreateUserResponse;
import com.soupulsar.modulith.auth.application.security.PasswordHasher;
import com.soupulsar.modulith.auth.domain.model.User;
import com.soupulsar.modulith.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordEncoder;

    public CreateUserResponse execute (CreateUserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.create(
                userRequest.name(),
                userRequest.cpf(),
                userRequest.telephone(),
                userRequest.email(),
                passwordEncoder.hash(userRequest.password()),
                userRequest.role()
        );

        userRepository.save(user);

        return new CreateUserResponse(user.getUserId());

    }
}