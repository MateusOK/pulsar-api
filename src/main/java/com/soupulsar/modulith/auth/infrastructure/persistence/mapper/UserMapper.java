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
        return entity;
    }

    public static User toModel(UserEntity entity){
        return User.restore(
                entity.getUserId(),
                entity.getName(),
                entity.getCpf(),
                entity.getTelephone(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getStatus()
        );
    }
}