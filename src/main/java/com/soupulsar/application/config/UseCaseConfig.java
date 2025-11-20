package com.soupulsar.application.config;

import com.soupulsar.application.security.JwtService;
import com.soupulsar.application.security.PasswordHasher;
import com.soupulsar.application.usecase.*;
import com.soupulsar.application.usecase.availability.CreateAvailabilityUseCase;
import com.soupulsar.application.usecase.session.CancelSessionUseCase;
import com.soupulsar.application.usecase.session.CompleteSessionUseCase;
import com.soupulsar.application.usecase.session.ConfirmSessionUseCase;
import com.soupulsar.application.usecase.session.ScheduleSessionUseCase;
import com.soupulsar.application.usecase.specialist.GetAllSpecialistsUseCase;
import com.soupulsar.application.usecase.specialist.GetDailyAvailabilityUseCase;
import com.soupulsar.application.usecase.specialist.GetSpecialistDetailsUseCase;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Bean
    public GetAllUsersUseCase getAllUsersUseCase(UserRepository userRepository) {
        return new GetAllUsersUseCase(userRepository);
    }

    @Bean
    public GetUserByIdUseCase getUserByIdUseCase(UserRepository userRepository) {
        return new GetUserByIdUseCase(userRepository);
    }

    @Bean
    public GetAllSpecialistsUseCase getAllSpecialistsUseCase(SpecialistProfileRepository specialistProfileRepository,
                                                             UserRepository userRepository,
                                                             SessionRepository sessionRepository) {
        return new GetAllSpecialistsUseCase(specialistProfileRepository, userRepository, sessionRepository);
    }

    @Bean
    GetSpecialistDetailsUseCase getSpecialistDetailsUseCase(SpecialistProfileRepository specialistRepository,
                                                     UserRepository userRepository,
                                                     SessionRepository sessionRepository) {
        return new GetSpecialistDetailsUseCase(specialistRepository, userRepository, sessionRepository);
    }

    @Bean
    GetDailyAvailabilityUseCase getDailyAvailabilityUseCase(SessionRepository sessionRepository,
                                                     AvailabilityRepository availabilityRepository) {
        return new GetDailyAvailabilityUseCase(sessionRepository, availabilityRepository);
    }

    @Bean
    public GetUserProfileUseCase getUserProfileUseCase(ClientProfileRepository clientProfileRepository,
                                                       SpecialistProfileRepository specialistProfileRepository,
                                                       UserRepository userRepository,
                                                       SecurityUtils securityUtils) {
        return new GetUserProfileUseCase(clientProfileRepository, specialistProfileRepository, userRepository, securityUtils);
    }

    @Bean
    public UpdateUserProfileUseCase updateUserProfileUseCase(ClientProfileRepository clientProfileRepository,
                                                             UserRepository userRepository,
                                                             SecurityUtils securityUtils) {
        return new UpdateUserProfileUseCase(userRepository, clientProfileRepository, securityUtils);
    }

    @Bean
    public SecurityUtils securityUtils(UserRepository userRepository) {
        return new SecurityUtils(userRepository);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, SecurityUtils securityUtils) {
        return new ChangePasswordUseCase(userRepository, passwordEncoder, securityUtils);
    }
}