package com.soupulsar.application.specialist.block;

import com.soupulsar.domain.exceptions.AvailabilityBlockNotFoundException;
import com.soupulsar.domain.model.availability.AvailabilityBlock;
import com.soupulsar.domain.repository.AvailabilityBlockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteAvailabilityBlockUseCase {

    private final AvailabilityBlockRepository availabilityBlockRepository;

    @Transactional
    public void execute(UUID id) {

        AvailabilityBlock availabilityBlock = availabilityBlockRepository.findById(id)
                .orElseThrow(AvailabilityBlockNotFoundException::new);

        availabilityBlockRepository.delete(availabilityBlock);
    }
}