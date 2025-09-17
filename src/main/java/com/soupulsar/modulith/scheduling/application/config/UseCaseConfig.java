package com.soupulsar.modulith.scheduling.application.config;

import com.soupulsar.modulith.scheduling.application.usecase.*;
import com.soupulsar.modulith.scheduling.domain.repository.AvailabilityRepository;
import com.soupulsar.modulith.scheduling.domain.repository.SessionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateAvailabilityUseCase createAvailabilityUseCase(AvailabilityRepository repository) {
        return new CreateAvailabilityUseCase(repository);
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
}