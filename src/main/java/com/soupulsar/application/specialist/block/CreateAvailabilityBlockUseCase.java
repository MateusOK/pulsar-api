package com.soupulsar.application.specialist.block;

import com.soupulsar.domain.exceptions.FutureSessionConflictException;
import com.soupulsar.domain.model.availability.AvailabilityBlock;
import com.soupulsar.domain.repository.AvailabilityBlockRepository;
import com.soupulsar.domain.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateAvailabilityBlockUseCase {

    private final AvailabilityBlockRepository availabilityBlockRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public CreateAvailabilityBlockResponse execute(CreateAvailabilityBlockRequest request) {

        AvailabilityBlock availabilityBlock = AvailabilityBlock.create(
                request.specialistId(),
                request.startsAt(),
                request.endsAt(),
                request.reason()
        );

        if (sessionRepository.countConflictingFutureSessionsForAvailabilityBlocks(availabilityBlock.getSpecialistId(),
                availabilityBlock.getStartsAt(), availabilityBlock.getEndsAt()) > 0) {
            throw new FutureSessionConflictException("Cannot create availability block with conflicting future sessions");
        }
        return new CreateAvailabilityBlockResponse(availabilityBlockRepository.save(availabilityBlock));
    }
}