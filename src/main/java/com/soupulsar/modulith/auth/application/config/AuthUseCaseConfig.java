package com.soupulsar.modulith.auth.application.config;

import com.soupulsar.modulith.auth.application.security.PasswordHasher;
import com.soupulsar.modulith.auth.application.usecase.AuthenticateUserUseCase;
import com.soupulsar.modulith.auth.application.usecase.RegisterUserUseCase;
import com.soupulsar.modulith.auth.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthUseCaseConfig {

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new AuthenticateUserUseCase(userRepository, passwordHasher);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new RegisterUserUseCase(userRepository, passwordHasher);
    }


}
