package com.soupulsar.application.dto.request;


import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record GetAllUsersRequest(

        Integer page,
        Integer size,
        UserStatus status,
        UserRole role
) {

    public static GetAllUsersRequest of(Integer page, Integer size, UserStatus status, UserRole role) {
        return new GetAllUsersRequest(page, size, status, role);
    }

    public Pageable toPageable(){
        int pageNumber = (page != null &&  page > 0) ? page : 0;
        int pageSize = (size != null  &&  size > 0) ? size : 10;
        return PageRequest.of(pageNumber, pageSize);
    }
}