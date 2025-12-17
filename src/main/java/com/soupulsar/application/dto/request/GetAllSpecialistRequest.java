package com.soupulsar.application.dto.request;

import com.soupulsar.domain.model.enums.SpecialistType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

@Schema(description = "Request DTO for retrieving all specialists with optional filters and pagination")
public record GetAllSpecialistRequest(
        @Schema(description = "Type of specialist to filter by", example = "PSICOLOGO")
        SpecialistType specialistType,
        @Schema(description = "Minimum price to filter by", example = "100.00")
        BigDecimal minPrice,
        @Schema(description = "Maximum price to filter by", example = "300.00")
        BigDecimal maxPrice,
        @Schema(description = "Page number for pagination", example = "0")
        Integer page,
        @Schema(description = "Number of items per page for pagination", example = "10")
        Integer size
) {

    public static GetAllSpecialistRequest of(SpecialistType specialistType, BigDecimal minPrice, BigDecimal maxPrice, Integer page, Integer size) {
        return new GetAllSpecialistRequest(specialistType, minPrice, maxPrice, page, size);
    }

    public Pageable toPageable() {
        int pageNumber = (page != null &&  page > 0) ? page : 0;
        int pageSize = (size != null  &&  size > 0) ? size : 10;
        return PageRequest.of(pageNumber, pageSize);
    }
}