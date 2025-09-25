package com.soupulsar.modulith.auth.infrastructure.persistence.mapper;

import com.soupulsar.modulith.auth.domain.model.User;
import com.soupulsar.modulith.auth.infrastructure.persistence.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserEntity toEntity(User user){
        UserEntity entity = new UserEntity();
        entity.setUserId(user.getUserId());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPasswordHash());
        entity.setRole(user.getRole());
        return entity;
    }

    public static User toModel(UserEntity entity){
        return User.restore(entity.getUserId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getTelephone(),
                entity.getCpf(),
                entity.getRole(),
                entity.getStatus());
    }

}