package com.soupulsar.application.config;

import com.soupulsar.application.interfaces.CustomerGateway;
import com.soupulsar.application.interfaces.EmailGateway;
import com.soupulsar.application.interfaces.PaymentGateway;
import com.soupulsar.application.interfaces.TokenGenerator;
import com.soupulsar.application.security.JwtService;
import com.soupulsar.application.security.PasswordHasher;
import com.soupulsar.application.usecase.auth.*;
import com.soupulsar.application.usecase.availability.CreateAvailabilityUseCase;
import com.soupulsar.application.usecase.payment.CreatePaymentUseCase;
import com.soupulsar.application.usecase.payment.HandlePaymentWebhookUseCase;
import com.soupulsar.application.usecase.payment.ProcessPaymentUseCase;
import com.soupulsar.application.usecase.session.CancelSessionUseCase;
import com.soupulsar.application.usecase.session.CompleteSessionUseCase;
import com.soupulsar.application.usecase.session.ConfirmSessionUseCase;
import com.soupulsar.application.usecase.session.ScheduleSessionUseCase;
import com.soupulsar.application.usecase.specialist.GetAllSpecialistsUseCase;
import com.soupulsar.application.usecase.specialist.GetDailyAvailabilityUseCase;
import com.soupulsar.application.usecase.specialist.GetSpecialistDashboardUseCase;
import com.soupulsar.application.usecase.specialist.GetSpecialistDetailsUseCase;
import com.soupulsar.application.usecase.user.GetAllUsersUseCase;
import com.soupulsar.application.usecase.user.GetUserByIdUseCase;
import com.soupulsar.application.usecase.user.GetUserProfileUseCase;
import com.soupulsar.application.usecase.user.UpdateUserProfileUseCase;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

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

    @Bean
    public CreatePaymentUseCase createPaymentUseCase(PaymentRepository paymentRepository, PaymentSplitRuleRepository paymentSplitRuleRepository,
                                         SpecialistProfileRepository specialistProfileRepository, UserRepository userRepository, SessionRepository sessionRepository) {
        return new CreatePaymentUseCase(paymentRepository, paymentSplitRuleRepository, specialistProfileRepository, userRepository, sessionRepository);
    }

    @Bean
    public ProcessPaymentUseCase processPaymentUseCase(PaymentRepository paymentRepository, ClientProfileRepository clientProfileRepository,
                                                       SpecialistProfileRepository specialistProfileRepository, UserRepository userRepository,
                                                       CustomerGateway customerGateway, PaymentGateway paymentGateway, SessionRepository sessionRepository) {
        return new ProcessPaymentUseCase(paymentRepository, clientProfileRepository, specialistProfileRepository,userRepository,
                customerGateway, paymentGateway, sessionRepository);
    }

    @Bean
    public HandlePaymentWebhookUseCase handlePaymentWebhookUseCase(WebhookEventRepository webhookRepository, PaymentRepository paymentRepository, ConfirmSessionUseCase confirmSessionUseCase) {
        return new HandlePaymentWebhookUseCase(webhookRepository, paymentRepository, confirmSessionUseCase);
    }

    @Bean
    public RequestPasswordResetUseCase requestPasswordResetUseCase (UserRepository userRepository, TokenGenerator tokenGenerator,
                                                                    PasswordResetTokenRepository passwordResetTokenRepository, EmailGateway emailGateway,
                                                                    @Value("${app.frontend.url}") String passwordResetUrl) {
        return new RequestPasswordResetUseCase(userRepository, tokenGenerator, passwordResetTokenRepository, emailGateway, passwordResetUrl);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase (PasswordEncoder passwordEncoder, UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository) {
        return new ResetPasswordUseCase(passwordEncoder, userRepository, passwordResetTokenRepository);
    }

    @Bean
    public GetSpecialistDashboardUseCase getSpecialistDashboardUseCase(SessionRepository sessionRepository, UserRepository userRepository, SecurityUtils securityUtils, Clock clock) {
        return new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, clock);
    }

}