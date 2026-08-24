package com.soupulsar.application.specialist.block;

import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.repository.AvailabilityBlockRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetSpecialistAvailabilityBlocksUseCase {

    private final AvailabilityBlockRepository repository;
    private final SecurityUtils securityUtils;

    public GetSpecialistAvailabilityBlocksResponse execute() {

        var specialistId = securityUtils.getCurrentUserId();

        var blocks = repository.findAllBySpecialistId(specialistId);

        return new GetSpecialistAvailabilityBlocksResponse(blocks);
    }
}