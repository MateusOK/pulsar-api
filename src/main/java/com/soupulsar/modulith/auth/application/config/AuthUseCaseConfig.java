package com.soupulsar.modulith.auth.application.config;

import com.soupulsar.modulith.auth.application.security.JwtService;
import com.soupulsar.modulith.auth.application.security.PasswordHasher;
import com.soupulsar.modulith.auth.application.usecase.AuthenticateUserUseCase;
import com.soupulsar.modulith.auth.application.usecase.RegistrationUseCase;
import com.soupulsar.modulith.auth.domain.repository.ClientProfileRepository;
import com.soupulsar.modulith.auth.domain.repository.SpecialistProfileRepository;
import com.soupulsar.modulith.auth.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthUseCaseConfig {

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, JwtService jwtService) {
        return new AuthenticateUserUseCase(userRepository, passwordHasher, jwtService);
    }

    @Bean
    public RegistrationUseCase registrationUseCase(UserRepository userRepository, ClientProfileRepository clientProfileRepository,
                                                   SpecialistProfileRepository specialistProfileRepository, PasswordHasher passwordHasher) {
        return new RegistrationUseCase(userRepository, clientProfileRepository, specialistProfileRepository,passwordHasher);
    }


}
