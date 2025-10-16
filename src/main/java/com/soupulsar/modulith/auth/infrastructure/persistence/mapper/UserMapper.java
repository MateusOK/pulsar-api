package com.soupulsar.modulith.auth.infrastructure.persistence.mapper;

import com.soupulsar.modulith.auth.domain.model.User;
import com.soupulsar.modulith.auth.infrastructure.persistence.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserEntity toEntity(User user){
        UserEntity entity = new UserEntity();
        entity.setName(user.getName());
        entity.setCpf(user.getCpf());
        entity.setTelephone(user.getTelephone());
        entity.setUserId(user.getUserId());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPasswordHash());
        entity.setStatus(user.getStatus());
        entity.setRole(user.getRole());
        entity.setAddress(AddressMapper.toEmbeddable(user.getAddress()));
        return entity;
    }

    public static User toModel(UserEntity entity){
        return User.builder()
                .userId(entity.getUserId())
                .name(entity.getName())
                .cpf(entity.getCpf())
                .telephone(entity.getTelephone())
                .email(entity.getEmail())
                .passwordHash(entity.getPassword())
                .role(entity.getRole())
                .status(entity.getStatus())
                .address(AddressMapper.toValueObject(entity.getAddress()))
                .build();
    }
}