package com.soupulsar.domain.model.user;

import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.model.vo.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class User {

    private final UUID userId;
    private final String name;
    private final String cpf;
    private String telephone;
    private String email;
    private String passwordHash;
    private Address address;
    private final UserRole role;
    private  UserStatus status;


    public static User create(String name, String cpf, String telephone, String email, String passwordHash, UserRole role, Address address) {
        if (name == null || name.isBlank()) throw  new IllegalArgumentException("Name cannot be null or blank");
        if (cpf == null || cpf.isBlank()) throw  new IllegalArgumentException("CPF cannot be null or blank");
        if (telephone == null || telephone.isBlank()) throw  new IllegalArgumentException("Telephone cannot be null or blank");
        if (email == null || email.isBlank()) throw  new IllegalArgumentException("Email cannot be null or blank");
        if (passwordHash == null || passwordHash.isBlank()) throw  new IllegalArgumentException("Password cannot be null or blank");
        return new User.UserBuilder()
                .userId(UUID.randomUUID())
                .name(name)
                .cpf(cpf)
                .telephone(telephone)
                .email(email)
                .passwordHash(passwordHash)
                .role(role)
                .status(UserStatus.ACTIVE)
                .address(address)
                .build();
    }

    public static User restore(UUID userId, String name, String cpf, String telephone, String email, String passwordHash, UserRole role, UserStatus status, Address address) {
        return new UserBuilder()
                .userId(userId)
                .name(name)
                .cpf(cpf)
                .telephone(telephone)
                .email(email)
                .passwordHash(passwordHash)
                .role(role)
                .status(status)
                .address(address)
                .build();
    }

    public void activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active.");
        }
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException("User is already inactive.");
        }
        this.status = UserStatus.INACTIVE;
    }
}