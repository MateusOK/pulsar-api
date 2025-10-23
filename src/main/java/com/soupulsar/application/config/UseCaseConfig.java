package com.soupulsar.application.config;

import com.soupulsar.application.security.JwtService;
import com.soupulsar.application.security.PasswordHasher;
import com.soupulsar.application.usecase.AuthenticateUserUseCase;
import com.soupulsar.application.usecase.RegistrationUseCase;
import com.soupulsar.application.usecase.availability.CreateAvailabilityUseCase;
import com.soupulsar.application.usecase.session.CancelSessionUseCase;
import com.soupulsar.application.usecase.session.CompleteSessionUseCase;
import com.soupulsar.application.usecase.session.ConfirmSessionUseCase;
import com.soupulsar.application.usecase.session.ScheduleSessionUseCase;
import com.soupulsar.domain.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateAvailabilityUseCase createAvailabilityUseCase(AvailabilityRepository availabilityRepository) {
        return new CreateAvailabilityUseCase(availabilityRepository);
    }

    @Bean
    public ScheduleSessionUseCase scheduleSessionUseCase(SessionRepository sessionRepository, AvailabilityRepository availabilityRepository, ApplicationEventPublisher publisher) {
        return new ScheduleSessionUseCase(sessionRepository, availabilityRepository, publisher);
    }

    @Bean
    public ConfirmSessionUseCase confirmSessionUseCase(SessionRepository sessionRepository) {
        return new ConfirmSessionUseCase(sessionRepository);
    }

    @Bean
    public CancelSessionUseCase cancelSessionUseCase(SessionRepository sessionRepository) {
        return new CancelSessionUseCase(sessionRepository);
    }

    @Bean
    public CompleteSessionUseCase completeSessionUseCase(SessionRepository sessionRepository) {
        return new CompleteSessionUseCase(sessionRepository);
    }
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