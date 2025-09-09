package com.soupulsar.modulith.scheduling.application.config;

import com.soupulsar.modulith.scheduling.application.usecase.CreateAvailabilityUseCase;
import com.soupulsar.modulith.scheduling.domain.repository.AvailabilityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateAvailabilityUseCase createAvailabilityUseCase(AvailabilityRepository repository) {
        return new CreateAvailabilityUseCase(repository);
    }

}
