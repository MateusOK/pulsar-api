package com.soupulsar.application.usecase;

import com.soupulsar.application.dto.request.GetAllUsersRequest;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
@Builder
public class GetAllUsersUseCase {

    private final UserRepository userRepository;

    public Page<User> execute(GetAllUsersRequest request) {
        return userRepository.findAll(request.status(), request.role(), request.toPageable());
    }
}