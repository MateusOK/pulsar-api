package com.soupulsar.infrastructure.persistence.specification;


import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.infrastructure.persistence.entity.user.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecifications {

    public static Specification<UserEntity> hasStatus(UserStatus status) {
        return (root, criteriaQuery, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<UserEntity> hasRole(UserRole role) {
        return (root, criteriaQuery, criteriaBuilder) ->
                role == null ? null : criteriaBuilder.equal(root.get("role"), role);
    }

}