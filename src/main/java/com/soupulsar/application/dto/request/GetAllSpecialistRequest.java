package com.soupulsar.application.dto.request;

import com.soupulsar.domain.model.enums.SpecialistType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public record GetAllSpecialistRequest(
        SpecialistType specialistType,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer page,
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